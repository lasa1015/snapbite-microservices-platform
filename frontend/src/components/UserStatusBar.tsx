// src/components/UserStatusBar.tsx
import { useUser } from "../context/UserContext";
import { useNavigate } from "react-router-dom";

export default function UserStatusBar() {
  const { username, setUsername } = useUser();
  const navigate = useNavigate();

  if (!username) return null;

  const handleLogout = () => {
    localStorage.clear();
    setUsername(null);
  };

  return (
    <div style={{ background: "#f5f5f5", padding: "0.5rem 1rem", display: "flex", justifyContent: "space-between" }}>
      <span>👤 Welcome, {username}</span>
      <div>
        <button onClick={() => navigate("/")}>🏠 Home</button>
        <button onClick={() => navigate("/my-orders")} style={{ marginLeft: "1rem" }}>📦 My Orders</button>
        <button onClick={handleLogout} style={{ marginLeft: "1rem" }}>Logout</button>
      </div>
    </div>
  );
}
