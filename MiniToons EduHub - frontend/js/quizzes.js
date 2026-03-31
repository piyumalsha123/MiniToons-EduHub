document.addEventListener("DOMContentLoaded", () => {

    const quizTableBody = document.getElementById("quizTableBody");
    const quizModalEl = document.getElementById("quizModal");
    const quizModal = new bootstrap.Modal(quizModalEl);
    const quizForm = quizModalEl.querySelector("form");

    const quizIdInput = document.getElementById("quizId");
    const quizTitleInput = document.getElementById("quizTitle");
    const quizUrlInput = document.getElementById("quizUrl");
    const quizCategoryInput = document.getElementById("quizCategory");
    const quizDifficultyInput = document.getElementById("quizDifficulty");
    const quizStatusInput = document.getElementById("quizStatus");

    let quizzes = [];


    function generateQuizId() {
        const existingNumbers = quizzes.map(q => parseInt(q.quiz_id.replace("Q", ""), 10));
        let i = 1;
        while (existingNumbers.includes(i)) i++;
        return `Q${String(i).padStart(3, "0")}`;
    }


    function loadQuizzes() {
        if (quizzes.length === 0) {
            quizTableBody.innerHTML = `<tr><td colspan="7" class="text-center">No quizzes available</td></tr>`;
            return;
        }

        quizTableBody.innerHTML = quizzes.map(q => `
            <tr data-id="${q.quiz_id}">
                <td>${q.quiz_id}</td>
                <td class="fw-bold text-start">${q.title}</td>
                <td><a href="${q.url}" target="_blank">Open</a></td>
                <td><span class="badge bg-success">${q.difficulty}</span></td>
                <td>${q.category}</td>
                <td><span class="${q.status === 'Active' ? 'status-active' : 'status-draft'}">${q.status}</span></td>
                <td>
                    <div class="action-btn btn-edit me-1" data-action="edit"><i class="bi bi-pencil-fill"></i></div>
                    <div class="action-btn btn-delete" data-action="delete"><i class="bi bi-trash3-fill"></i></div>
                </td>
            </tr>
        `).join("");
    }


    function resetQuizForm() {
        quizForm.reset();
        quizIdInput.value = "";
    }


    quizForm.addEventListener("submit", async (e) => {
        e.preventDefault();

        const quizData = {
            title: quizTitleInput.value,
            url: quizUrlInput.value,
            category: quizCategoryInput.value,
            difficulty: quizDifficultyInput.value,
            status: quizStatusInput.value
        };

        try {
            if (quizIdInput.value) {

                const res = await fetch(`http://localhost:8080/api/v1/quizzes/update/${quizIdInput.value}`, {
                    method: "PUT",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(quizData)
                });

                if (!res.ok) throw new Error("Update failed");
                alert("Quiz updated!");
            } else {

                quizData.quizId = generateQuizId();

                const res = await fetch("http://localhost:8080/api/v1/quizzes/save", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(quizData)
                });

                if (!res.ok) throw new Error("Save failed");
                alert("Quiz added!");
            }

            resetQuizForm();
            quizModal.hide();
            await loadQuizzesFromServer();
        } catch (err) {
            console.error(err);
            alert("Error saving quiz!");
        }
    });

    quizTableBody.addEventListener("click", async (e) => {
        const btn = e.target.closest("[data-action]");
        if (!btn) return;

        const tr = btn.closest("tr");
        const id = tr.dataset.id;
        const q = quizzes.find(q => q.quiz_id === id);

        if (btn.dataset.action === "edit") {
            quizIdInput.value = q.quiz_id;
            quizTitleInput.value = q.title;
            quizUrlInput.value = q.url;
            quizCategoryInput.value = q.category;
            quizDifficultyInput.value = q.difficulty;
            quizStatusInput.value = q.status;
            quizModal.show();
        }

        if (btn.dataset.action === "delete") {
            if (!confirm("Delete this quiz?")) return;

            try {
                const res = await fetch(`http://localhost:8080/api/v1/quizzes/delete/${id}`, {
                    method: "DELETE"
                });

                if (!res.ok) throw new Error("Delete failed");
                alert("Deleted!");
                await loadQuizzesFromServer();
            } catch (err) {
                console.error(err);
                alert("Delete failed!");
            }
        }
    });


    async function loadQuizzesFromServer() {
        try {
            const res = await fetch("http://localhost:8080/api/v1/quizzes");
            if (!res.ok) throw new Error("Failed to fetch quizzes");
            const data = await res.json();

            quizzes = data.map(q => ({
                quiz_id: q.quizId,
                title: q.title,
                url: q.url,
                category: q.category,
                difficulty: q.difficulty,
                status: q.status
            }));

            loadQuizzes();
        } catch (err) {
            console.error(err);
            quizTableBody.innerHTML = `<tr><td colspan="7" class="text-center">Failed to load quizzes</td></tr>`;
        }
    }


    loadQuizzesFromServer();


    const quizContainer = document.getElementById('quizContainer');

    async function loadQuizzesForChild() {
        quizContainer.innerHTML = "<p>Loading quizzes...</p>";

        try {
            const childId = 123; // dynamically get logged-in child ID
            const res = await fetch(`/api/child/${childId}/quizzes`);

            if (!res.ok) throw new Error("Failed to fetch child quizzes");

            const data = await res.json();
            quizContainer.innerHTML = "";

            if (!data.length) {
                quizContainer.innerHTML = "<p>No quizzes assigned yet.</p>";
                return;
            }

            data.forEach(q => {
                const col = document.createElement('div');
                col.className = "col-md-4 col-sm-6 mb-4";

                col.innerHTML = `
                    <div class="card h-100 shadow-sm p-3 text-center">
                        <div class="mb-3" style="font-size: 3rem;">🧠</div>
                        <h5 class="fw-bold">${q.title}</h5>
                        <p class="text-muted small">${q.category} • ${q.difficulty}</p>
                        <button class="btn btn-primary btn-sm mt-2">Play Now 🚀</button>
                    </div>
                `;

                col.querySelector("button").addEventListener("click", () => {
                    window.location.href = q.url;
                });

                quizContainer.appendChild(col);
            });

        } catch (err) {
            console.error(err);
            quizContainer.innerHTML = "<p>Failed to load quizzes.</p>";
        }
    }


    loadQuizzesForChild();
});