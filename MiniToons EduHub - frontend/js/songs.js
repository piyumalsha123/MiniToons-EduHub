// ====== Songs Module ======
const songTableBody = document.querySelector('#songTableBody');
const songModalEl = document.getElementById('songModal');
const songModal = new bootstrap.Modal(songModalEl);
const songForm = songModalEl.querySelector('form');

const API = "http://localhost:8080/api/v1/songs";
let songs = [];
let editSongId = null; // Track current song being edited

async function loadSongs() {
    try {
        const res = await fetch(API);
        songs = await res.json();
        renderSongs();
    } catch (err) {
        console.error('Failed to load songs:', err);
    }
}

function renderSongs() {
    songTableBody.innerHTML = '';
    songs.forEach(song => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td class="fw-bold text-muted">#${song.songId}</td>
            <td class="text-start"><span class="fw-bold">${song.title}</span></td>
            <td>${song.ageGroup}</td>
            <td><span class="badge-date">${song.createdAt ? song.createdAt.split('T')[0] : ''}</span></td>
            <td><span class="status-${song.status.toLowerCase()}">${song.status}</span></td>
              <td>
                <a href="${song.videoUrl}" target="_blank" class="text-primary fw-bold">
                    Open
                </a>
            </td>
            <td>
                <div class="action-btn btn-edit me-1"><i class="bi bi-pencil-fill"></i></div>
                <div class="action-btn btn-delete"><i class="bi bi-trash3-fill"></i></div>
            </td>
        `;

        row.querySelector('.btn-edit').addEventListener('click', () => editSong(song.songId));

        row.querySelector('.btn-delete').addEventListener('click', () => deleteSong(song.songId));

        songTableBody.appendChild(row);
    });
}

async function editSong(id) {
    try {
        const res = await fetch(`${API}/${id}`);
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        const song = await res.json();

        document.getElementById('songId').value = song.songId;
        document.getElementById('title').value = song.title;
        document.getElementById('ageGroup').value = song.ageGroup;
        document.getElementById('videoUrl').value = song.videoUrl;
        document.getElementById('lyrics').value = song.lyrics;
        document.getElementById('createdAt').value = song.createdAt ? song.createdAt.split('T')[0] : '';
        document.getElementById('status').value = song.status;

        editSongId = id;
        songModal.show();
    } catch (err) {
        console.error('Failed to load song for edit:', err);
        alert('Failed to load song. Check console.');
    }
}

async function deleteSong(id) {
    if (!confirm('Are you sure you want to delete this song?')) return;

    try {
        const res = await fetch(`${API}/delete/${id}`, { method: 'DELETE' });
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        songs = songs.filter(s => s.songId !== id);
        renderSongs();
    } catch (err) {
        console.error('Failed to delete song:', err);
        alert('Failed to delete song.');
    }
}

songForm.addEventListener('submit', async (e) => {
    e.preventDefault();

    const songData = {
        title: document.getElementById('title').value,
        ageGroup: document.getElementById('ageGroup').value,
        videoUrl: document.getElementById('videoUrl').value,
        lyrics: document.getElementById('lyrics').value,
        status: document.getElementById('status').value,
        createdAt: document.getElementById('createdAt').value || null
    };

    try {
        if (editSongId) {
            const res = await fetch(`${API}/update/${editSongId}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(songData)
            });
            if (!res.ok) throw new Error(`HTTP ${res.status}`);
            const updated = await res.json();
            const index = songs.findIndex(s => s.songId === editSongId);
            songs[index] = updated;
            editSongId = null;
            alert('Song updated successfully!');
        } else {
            const res = await fetch(`${API}/save`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(songData)
            });
            if (!res.ok) throw new Error(`HTTP ${res.status}`);
            const newSong = await res.json();
            songs.push(newSong);
            alert('Song added successfully!');
        }

        songForm.reset();
        songModal.hide();
        renderSongs();
    } catch (err) {
        console.error('Failed to save song:', err);
        alert('Failed to save song. Check console.');
    }
});

loadSongs();