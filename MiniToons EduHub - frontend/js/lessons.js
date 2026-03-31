
const lessonTableBody = document.querySelector('#lesson-tab tbody');
const lessonModalEl = document.getElementById('lessonModal');
const lessonModal = new bootstrap.Modal(lessonModalEl);
const lessonForm = lessonModalEl.querySelector('form');

let lessons = [];
const API_URL = 'http://localhost:8080/api/v1/lessons';

async function loadLessons() {
    try {
        const res = await fetch(API_URL);
        lessons = await res.json();
        renderLessons();
    } catch (err) {
        console.error('Failed to load lessons:', err);
    }
}

function renderLessons() {
    lessonTableBody.innerHTML = '';
    lessons.forEach((lesson) => {
        const row = document.createElement('tr');
        row.innerHTML = `
    <td class="fw-bold text-muted">${lesson.lessonId}</td>
    <td class="text-start"><span class="fw-bold">${lesson.title}</span></td>
    <td>${lesson.category}</td>
    <td>${lesson.ageGroup}</td> <!-- NEW COLUMN -->
    <td><span class="badge-date">${lesson.createdDate}</span></td>
    <td><span class="status-${lesson.status.toLowerCase()}">${lesson.status}</span></td>
        <td>
                <a href="${lesson.contentUrl}" target="_blank" class="text-primary fw-bold">
                    Open
                </a>
            </td>
            <td>
                <div class="action-btn btn-edit me-1">
                    <i class="bi bi-pencil-fill"></i>
                </div>
                <div class="action-btn btn-delete">
                    <i class="bi bi-trash3-fill"></i>
                </div>
            </td>
`;
        row.querySelector('.btn-edit').addEventListener('click', () => editLesson(lesson.lessonId));
        row.querySelector('.btn-delete').addEventListener('click', () => deleteLesson(lesson.lessonId));
        lessonTableBody.appendChild(row);
    });
}

// Handle add/edit lesson form submit
lessonForm.addEventListener('submit', async (e) => {
    e.preventDefault();

    const lessonData = {
        title: document.getElementById('lessonTitle').value,
        lessonId: document.getElementById('lessonId').value,
        category: document.getElementById('lessonCategory').value,
        difficulty: document.getElementById('lessonDifficulty').value,
        ageGroup: document.getElementById('lessonAgeGroup').value,
        createdDate: document.getElementById('lessonCreatedDate').value,
        status: document.getElementById('lessonStatus').value,
        contentUrl: document.getElementById('lessonUrl').value // 🔥 ADD
    };
    try {
        if (!lessonData.lessonId) {
            const res = await fetch(`${API_URL}/save`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(lessonData)
            });
            const newLesson = await res.json();
            lessons.push(newLesson);
        } else {
            const res = await fetch(`${API_URL}/${lessonData.lessonId}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(lessonData)
            });
            const updatedLesson = await res.json();
            const index = lessons.findIndex(l => l.lessonId === lessonData.lessonId);
            lessons[index] = updatedLesson;
        }

        renderLessons();
        lessonForm.reset();
        lessonModal.hide();
    } catch (err) {
        console.error('Failed to save lesson:', err);
    }
});

function editLesson(lessonId) {
    const lesson = lessons.find(l => l.lessonId === lessonId);
    if (!lesson) return;

    document.getElementById('lessonTitle').value = lesson.title;
    document.getElementById('lessonId').value = lesson.lessonId;
    document.getElementById('lessonCategory').value = lesson.category;
    document.getElementById('lessonDifficulty').value = lesson.difficulty;
    document.getElementById('lessonAgeGroup').value = lesson.ageGroup;
    document.getElementById('lessonCreatedDate').value = lesson.createdDate;
    document.getElementById('lessonStatus').value = lesson.status;
    document.getElementById('lessonUrl').value = lesson.contentUrl;

    lessonModal.show();
}

async function deleteLesson(lessonId) {
    if (!confirm('Are you sure you want to delete this lesson?')) return;

    try {
        const res = await fetch(`${API_URL}/delete/${lessonId}`, { method: 'DELETE' });
        if (!res.ok) throw new Error('Delete failed');

        // remove from frontend list
        lessons = lessons.filter(l => l.lessonId !== lessonId);
        renderLessons();
        alert('Lesson deleted successfully ✅');
    } catch (err) {
        console.error('Failed to delete lesson:', err);
        alert('Failed to delete lesson ❌');
    }
}

// Initial load
loadLessons();