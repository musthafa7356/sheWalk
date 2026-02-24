document.getElementById("registerForm").addEventListener("submit", async function (e) {
    e.preventDefault();
    const regBtn = document.getElementById("regBtn");
    const modal = document.getElementById("validationModal");
    const modalTitle = document.getElementById("modalTitle");
    const modalMsg = document.getElementById("validationMsg");
    const modalBtn = document.getElementById("modalBtn");

    // Clear UI
    document.querySelectorAll(".error-text").forEach(el => el.innerText = "");
    document.querySelectorAll("input, select").forEach(el => el.classList.remove("input-error"));

    const name = document.getElementById("name").value.trim();
    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value;
    const confirmPassword = document.getElementById("confirmPassword").value;
    const trustedEmail = document.getElementById("trustedEmail").value.trim();
    const role = document.getElementById("role").value;

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    // Custom Show Modal Function
    const showManualPopup = (title, message, isSuccess = false) => {
        modalTitle.innerText = title;
        modalTitle.style.color = isSuccess ? "#27ae60" : "#e74c3c";
        modalMsg.innerText = message;
        modal.style.display = "flex";

        if (isSuccess) {
            modalBtn.onclick = () => window.location.href = "login.html";
        } else {
            modalBtn.onclick = () => modal.style.display = "none";
        }
    };

    const triggerError = (id, message) => {
        document.getElementById(id).classList.add("input-error");
        document.getElementById(id + "Error").innerText = message;
        showManualPopup("Validation Error", message);
        return true;
    };

    // Validations
    if (!name) return triggerError("name", "Name is required");
    if (!emailRegex.test(email)) return triggerError("email", "Valid email required (e.g. name@gmail.com)");
    if (password.length < 8) return triggerError("password", "Password must be at least 8 characters");
    if (password !== confirmPassword) return triggerError("confirmPassword", "Passwords do not match");
    if (!emailRegex.test(trustedEmail)) return triggerError("trustedEmail", "Valid trusted email required");
    if (!role) return triggerError("role", "Please select a role");

    const payload = { name, email, password, trustedEmail, role };

    regBtn.disabled = true;
    regBtn.innerText = "Registering...";

    try {
        const response = await fetch("/auth/register", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            showManualPopup("Success!", "Registration successful! Click OK to log in.", true);
        } else {
            const data = await response.json();
            showManualPopup("Registration Failed", data.error || "Please check your details.");
        }
    } catch (error) {
        showManualPopup("Server Error", "Could not connect to server. Please try again later.");
    } finally {
        regBtn.disabled = false;
        regBtn.innerText = "Sign Up";
    }
});

// Helper for close button
window.closeValidation = () => {
    document.getElementById("validationModal").style.display = "none";
};

