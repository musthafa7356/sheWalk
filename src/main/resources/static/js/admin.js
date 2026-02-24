let currentSessionId = null; // Global variable to track which session we want to stop

document.addEventListener("DOMContentLoaded", () => {
    const token = sessionStorage.getItem("token");
    if (!token) {
        alert("Session expired. Please login again.");
        window.location.href = "login.html";
        return;
    }

    loadDashboardData();
    setInterval(loadActiveSessions, 10000);

    // Set up the Modal Confirm button listener once
    document.getElementById("confirmStopBtn").addEventListener("click", executeForceStop);
});

async function loadDashboardData() {
    await loadUsers();
    await loadActiveSessions();
    await loadHistory();
}

async function loadUsers() {
    try {
        const response = await fetch('/api/admin/users', {
            headers: { 'Authorization': 'Bearer ' + sessionStorage.getItem('token') }
        });
        const users = await response.json();
        const list = document.getElementById("userList");
        if (!list) return;

        list.innerHTML = users.length === 0
            ? "<tr><td colspan='4' style='text-align:center'>No users found</td></tr>"
            : users.map(user => `
                <tr>
                    <td>${user.name}</td>
                    <td>${user.email}</td>
                    <td>${user.role}</td>
                    <td>${user.trustedEmail || 'None'}</td>
                </tr>
            `).join('');
    } catch (e) { console.error("Error fetching users:", e); }
}

async function loadActiveSessions() {
    try {
        const response = await fetch('/api/admin/session/active', {
            headers: { 'Authorization': 'Bearer ' + sessionStorage.getItem('token') }
        });
        const sessions = await response.json();
        const list = document.getElementById("activeSessionsList");
        if (!list) return;

        list.innerHTML = sessions.length === 0
            ? "<tr><td colspan='4' style='text-align:center'>No active emergencies</td></tr>"
            : sessions.map(s => `
                <tr>
                    <td>${s.userName}</td>
                    <td>${new Date(s.startedAt).toLocaleString()}</td>
                    <td><a href="/track.html?token=${s.trackingToken}" target="_blank" class="view-link">View Live Map</a></td>
                    <td><button class="btn-stop" onclick="openStopModal(${s.id})">Force Stop</button></td>
                </tr>
            `).join('');
    } catch (e) { console.error("Error fetching sessions:", e); }
}

async function loadHistory() {
    try {
        const response = await fetch('/api/admin/sessions/history', {
            headers: { 'Authorization': 'Bearer ' + sessionStorage.getItem('token') }
        });
        const sessions = await response.json();
        const list = document.getElementById("historyList");
        if (!list) return;

        list.innerHTML = sessions.length === 0
            ? "<tr><td colspan='5' style='text-align:center'>No history available</td></tr>"
            : sessions.map(s => `
                <tr>
                    <td>${s.userName}</td>
                    <td>${new Date(s.startedAt).toLocaleString()}</td>
                    <td>${s.endedAt ? new Date(s.endedAt).toLocaleString() : '-'}</td>
                    <td>${s.active ? 'Active' : 'Completed'}</td>
                    <td>${s.duration || 'N/A'}</td>
                </tr>
            `).join('');
    } catch (e) { console.error("Error loading history:", e); }
}

/* ================= MODAL LOGIC ================= */

function openStopModal(sessionId) {
    currentSessionId = sessionId; // Store the ID globally
    document.getElementById("stopModal").classList.add("active");
}

function closeModal() {
    currentSessionId = null;
    document.getElementById("stopModal").classList.remove("active");
}

async function executeForceStop() {
    if (!currentSessionId) return;

    try {
        const response = await fetch(`/api/admin/sessions/${currentSessionId}/stop`, {
            method: 'POST',
            headers: { 'Authorization': 'Bearer ' + sessionStorage.getItem('token') }
        });

        if (response.ok) {
            closeModal();
            loadActiveSessions();
            loadHistory();
        } else {
            alert("Error stopping session.");
        }
    } catch (error) {
        console.error("Error stopping session:", error);
    }
}