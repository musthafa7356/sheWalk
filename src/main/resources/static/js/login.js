window.handleLogin = async function() {
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    const messageEl = document.getElementById("message");
    const loginBtn = document.getElementById("loginBtn");

    // UI State: Loading
    loginBtn.innerText = "Authenticating...";
    loginBtn.disabled = true;
    messageEl.innerText = "";

    try {
        const response = await fetch("/auth/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email, password })
        });

        // 1. Handle Error Text from Backend
        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || "Login failed");
        }

        // 2. Parse JSON Token
        const data = await response.json();

        if (data && data.token) {
            sessionStorage.setItem("token", data.token);

            // 3. Decode JWT to find Role (so Admins go to Admin page)
            try {
                const payload = JSON.parse(atob(data.token.split('.')[1]));
                const userRole = payload.role || payload.roles || "";

                if (userRole.includes("ADMIN")) {
                    window.location.href = "/admin.html";
                } else {
                    window.location.href = "/home.html";
                }
            } catch (decodeErr) {
                // Fallback if decoding fails
                window.location.href = "/home.html";
            }
        }

    } catch (err) {
        console.error("Login Error:", err.message);
        loginBtn.innerText = "Log In";
        loginBtn.disabled = false;

        // UI Feedback for specific errors
        if (err.message.toLowerCase().includes("credentials") || err.message.includes("401")) {
            messageEl.innerText = "Wrong email or password. Please try again.";
            messageEl.style.color = "orange";
        } else {
            messageEl.innerText = "Server Error: Is the backend running?";
            messageEl.style.color = "red";
        }
    }
};