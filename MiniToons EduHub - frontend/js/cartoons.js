const cartoonTableBody = document.getElementById("cartoonTableBody");

const cartoonModalEl = document.getElementById("cartoonModal");
const cartoonModal = new bootstrap.Modal(cartoonModalEl);
const cartoonForm = cartoonModalEl.querySelector("form");

const idInput = document.getElementById("cartoonId");
const titleInput = document.getElementById("cartoonTitle");
const videoUrlInput = document.getElementById("cartoonVideoUrl");
const ageGroupInput = document.getElementById("cartoonAgeGroup");
const createdAtInput = document.getElementById("cartoonCreatedAt");
const statusInput = document.getElementById("cartoonStatus");

let cartoons = [];


function loadCartoons() {
    cartoonTableBody.innerHTML = cartoons.map(c => `
        <tr>
            <td>${c.cartoonId}</td> <td>${c.title}</td>
            <td>${Array.isArray(c.category) ? c.category.join(", ") : c.category}</td>
            <td><a href="${c.videoUrl}" target="_blank">Video</a></td>
            <td>${c.ageGroup}</td>
            <td>${c.createdAt}</td>
            <td>${c.status}</td>
            <td>
                <div class="action-btn btn-edit me-1" onclick="editCartoon('${c.cartoonId}')">
                    <i class="bi bi-pencil-fill"></i>
                </div>
                <div class="action-btn btn-delete" onclick="deleteCartoon('${c.cartoonId}')">
                    <i class="bi bi-trash3-fill"></i>
                </div>
            </td>
        </tr>
    `).join("");
}

async function loadCartoonsFromServer() {
    try {
        const res = await fetch("http://localhost:8080/api/v1/cartoons");
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        const data = await res.json();

        cartoons = data.map(c => ({
            cartoonId: c.cartoonId,
            title: c.title,
            category: (c.category || "").split(", "),
            videoUrl: c.videoUrl,
            ageGroup: c.ageGroup,
            createdAt: c.createdAt,
            status: c.status
        }));

        loadCartoons();
    } catch (err) {
        console.error("Failed to load cartoons:", err);
        cartoonTableBody.innerHTML = `<tr><td colspan="8">Failed to load cartoons.</td></tr>`;
    }
}

loadCartoonsFromServer();

function resetForm() {
    cartoonForm.reset();
    idInput.value = "";
    document.querySelectorAll("#cartoonForm input[type='checkbox']").forEach(cb => cb.checked = false);
}


document.querySelector('[data-bs-target="#cartoonModal"]').addEventListener("click", () => {
    resetForm();
    cartoonModal.show();
});

cartoonForm.addEventListener("submit", async function(e) {
    e.preventDefault();

    const selectedCategories = Array.from(
        document.querySelectorAll("#cartoonForm input[type='checkbox']:checked")
    ).map(cb => cb.value);


    const cartoonData = {
        title: titleInput.value,
        category: selectedCategories.join(", "),
        videoUrl: videoUrlInput.value,
        ageGroup: ageGroupInput.value,
        createdAt: createdAtInput.value || null,
        status: statusInput.value
    };

    try {
        const currentId = idInput.value;


        if (currentId && cartoons.some(c => c.cartoonId === currentId)) {

            const res = await fetch(`http://localhost:8080/api/v1/cartoons/update/${currentId}`, {
                method: "PUT",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(cartoonData)
            });
            if (!res.ok) throw new Error(`HTTP ${res.status}`);
            alert("Cartoon updated successfully! 🎉");
        } else {

            const res = await fetch("http://localhost:8080/api/v1/cartoons/save", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(cartoonData)
            });
            if (!res.ok) throw new Error(`HTTP ${res.status}`);
            alert("Cartoon added successfully! 🎉");
        }

        resetForm();
        cartoonModal.hide();
        await loadCartoonsFromServer();
    } catch (err) {
        console.error("Failed to save cartoon:", err);
        alert("Failed to save cartoon! See console.");
    }
});

function editCartoon(id) {
    const c = cartoons.find(ct => ct.cartoonId === id);
    if (!c) return;

    idInput.value = c.cartoonId;
    titleInput.value = c.title;
    videoUrlInput.value = c.videoUrl;
    ageGroupInput.value = c.ageGroup;
    createdAtInput.value = c.createdAt;
    statusInput.value = c.status;

    document.querySelectorAll("#cartoonForm input[type='checkbox']").forEach(cb => cb.checked = false);
    const categories = Array.isArray(c.category) ? c.category : (c.category ? c.category.split(", ") : []);
    categories.forEach(cat => {
        document.querySelectorAll("#cartoonForm input[type='checkbox']").forEach(cb => {
            if (cb.value === cat) cb.checked = true;
        });
    });

    cartoonModal.show();
}

async function deleteCartoon(id) {
    if (!confirm("Are you sure you want to delete this cartoon? ❌")) return;

    try {
        const res = await fetch(`http://localhost:8080/api/v1/cartoons/delete/${id}`, {
            method: "DELETE"
        });
        if (!res.ok) throw new Error(`HTTP ${res.status}`);

        alert("Cartoon deleted! 🗑️");
        await loadCartoonsFromServer(); // reload table
    } catch (err) {
        console.error("Failed to delete cartoon:", err);
        alert("Failed to delete cartoon! See console.");
    }
}