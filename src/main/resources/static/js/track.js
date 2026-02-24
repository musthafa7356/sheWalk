const urlParams = new URLSearchParams(window.location.search);
const token = urlParams.get("token");

const map = L.map('map').setView([0, 0], 2);
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '© OpenStreetMap contributors'
}).addTo(map);

let marker;
let trackingInterval;
let currentLat = null;
let currentLng = null;

const directionBtn = document.getElementById("directionBtn");
const statusMsg = document.getElementById("statusMsg");
const locationBox = document.getElementById("locationBox");

directionBtn.addEventListener("click", () => {
    if (currentLat && currentLng) {
        // FIXED URL FORMATTING
        const url = `https://www.google.com/maps?q=${currentLat},${currentLng}`;
        window.open(url, "_blank");
    }
});

async function fetchLocation() {
    if (!token) {
        statusMsg.innerHTML = "<h2 style='color:red;'>Tracking Token Missing</h2>";
        statusMsg.style.display = "block";
        return;
    }

    try {
        // Use your relative path or http://localhost:8080 depending on your environment
        const response = await fetch(`/track/${token}`);

        if (!response.ok) {
            statusMsg.innerHTML = "<h2 style='color:red;'>Emergency inactive or link expired.</h2>";
            statusMsg.style.display = "block";
            locationBox.style.display = "none";
            if (trackingInterval) clearInterval(trackingInterval);
            return;
        }

        const data = await response.json();
        currentLat = data.latitude;
        currentLng = data.longitude;

        // Update UI Text
        document.getElementById("lat").innerText = currentLat.toFixed(6);
        document.getElementById("lon").innerText = currentLng.toFixed(6);
        document.getElementById("time").innerText = new Date().toLocaleTimeString();
        locationBox.style.display = "block";

        // Update Map Marker
        if (!marker) {
            marker = L.marker([currentLat, currentLng]).addTo(map);
            map.setView([currentLat, currentLng], 15);
        } else {
            marker.setLatLng([currentLat, currentLng]);
        }

        directionBtn.style.display = "block";

    } catch (error) {
        console.error("Error fetching location:", error);
    }
}

fetchLocation();
trackingInterval = setInterval(fetchLocation, 5000);

