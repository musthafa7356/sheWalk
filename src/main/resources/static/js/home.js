document.addEventListener("DOMContentLoaded", () => {
    const token = sessionStorage.getItem("token");
    if (!token) {
        window.location.href = "login.html";
        return;
    }

    const safeModeUI = document.getElementById("safeMode");
    const emergencyModeUI = document.getElementById("emergencyMode");
    const sosCard = document.getElementById("sosCard");
    const msgEl = document.getElementById("emergencyMessage");
    const logoutModal = document.getElementById("logoutModal");
    const alertModal = document.getElementById("alertModal");
    const alertMessage = document.getElementById("alertMessage");

    loadInitialData(token);

    function toggleSOSUI(isEmergency) {
        if (isEmergency) {
            safeModeUI.style.display = "none";
            emergencyModeUI.style.display = "block";
            sosCard.classList.add("pulse-active");
        } else {
            safeModeUI.style.display = "block";
            emergencyModeUI.style.display = "none";
            sosCard.classList.remove("pulse-active");
        }
    }

    // --- Custom Alert Logic ---
    window.showAlert = (message) => {
        alertMessage.innerText = message;
        alertModal.style.display = "flex";
    };

    window.closeAlert = () => {
        alertModal.style.display = "none";
    };

    // --- SOS Start Logic ---
    let watchId = null; // 1. Add this variable at the top to store the tracker

        document.getElementById("startEmergencyBtn").onclick = () => {
            if (!navigator.geolocation) {
                showAlert("Geolocation not supported.");
                return;
            }

            msgEl.innerText = "📍 Getting GPS location...";

            // 2. Change getCurrentPosition to watchPosition
            watchId = navigator.geolocation.watchPosition(async (position) => {
                const lat = position.coords.latitude;
                const lng = position.coords.longitude;

                toggleSOSUI(true);
                msgEl.innerText = "📡 Tracking Active. Location shared.";

                try {
                    // 3. This will now hit your API every time the user moves
                    const response = await fetch("/api/emergency/start", {
                        method: "POST",
                        headers: {
                            "Authorization": "Bearer " + token,
                            "Content-Type": "application/json"
                        },
                        body: JSON.stringify({
                            latitude: lat,
                            longitude: lng
                        })
                    });

                    if (!response.ok) {
                        const errorData = await response.json().catch(() => ({}));
                        console.error("Sync error:", errorData.error);
                    }
                } catch (error) {
                    console.error("Server error during tracking update.");
                }
            }, (error) => {
                showAlert("Location access lost. Please keep GPS enabled.");
                console.error(error);
            }, {
                enableHighAccuracy: true,
                maximumAge: 0,
                timeout: 10000
            });
        };

        // --- SOS Stop Logic ---
        document.getElementById("stopEmergencyBtn").onclick = async () => {
            // 4. IMPORTANT: Stop the GPS watcher when the user clicks 'Stop'
            if (watchId !== null) {
                navigator.geolocation.clearWatch(watchId);
                watchId = null;
            }

            try {
                const response = await fetch("/api/emergency/stop", {
                    method: "POST",
                    headers: { "Authorization": "Bearer " + token }
                });
                if (response.ok) {
                    toggleSOSUI(false);
                    msgEl.innerText = "🟢 You are marked safe.";
                } else {
                    showAlert("Failed to stop session.");
                }
            } catch (error) {
                showAlert("Server error.");
            }
        };

    // --- Logout Logic ---
    document.getElementById("logoutBtn").onclick = () => {
        logoutModal.style.display = "flex";
    };

    window.closeLogoutModal = () => {
        logoutModal.style.display = "none";
    };

    window.confirmLogout = () => {
        sessionStorage.removeItem("token");
        window.location.href = "login.html";
    };

    // Close modals when clicking outside the box
    window.onclick = (e) => {
        if (e.target === logoutModal) closeLogoutModal();
        if (e.target === alertModal) closeAlert();
    };
});

async function loadInitialData(token) {
    try {
        const profRes = await fetch("/users/api/profile", {
            headers: { "Authorization": "Bearer " + token }
        });
        const profile = await profRes.json();

        document.getElementById("profileName").innerText = profile.name;
        document.getElementById("profileEmail").innerText = profile.email;

        if (profile.role && profile.role.includes("ADMIN")) {
            document.getElementById("adminBtn").style.display = "inline-block";
        }

        const welcomeRes = await fetch("/home", {
            headers: { "Authorization": "Bearer " + token }
        });
        document.getElementById("welcome").innerText = await welcomeRes.text();
    } catch (err) {
        console.error("Initialization failed");
    }
}