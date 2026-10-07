import { useState } from "react";

function Login({ onLogin }) {

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");

   const handleLogin = async (e) => {
    e.preventDefault();

    setError("");

    try {
        const response = await fetch(
            "http://localhost:8080/api/auth/login",
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    username: username,
                    password: password
                })
            }
        );

        console.log("Login status:", response.status);

        const responseText = await response.text();

        console.log("Login response:", responseText);

        if (!response.ok) {
            throw new Error(
                `Login failed (${response.status}): ${responseText}`
            );
        }

        const data = JSON.parse(responseText);

        localStorage.setItem("token", data.token);
        localStorage.setItem("username", data.username);
        localStorage.setItem("role", data.role);

        onLogin(data);

    } catch (error) {
        console.error("Login error:", error);
        setError(error.message);
    }
};

    return (
        <div className="login-container">

            <h2>Supply Chain Routing System</h2>

            <form onSubmit={handleLogin}>

                <input
                    type="text"
                    placeholder="Username"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                />

                <input
                    type="password"
                    placeholder="Password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />

                <button type="submit">
                    Login
                </button>

            </form>

            {error && (
                <p style={{ color: "red" }}>
                    {error}
                </p>
            )}

        </div>
    );
}

export default Login;