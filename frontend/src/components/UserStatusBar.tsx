import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useUser } from "../context/UserContext";
import AuthModal from "./AuthModal";

export default function UserStatusBar() {
  const { username, role, setUsername, setRole } = useUser(); // ✅ 增加 role 和 setRole
  const navigate = useNavigate();
  const [authMode, setAuthMode] = useState<"login" | "register" | null>(null);

  const handleLogout = () => {
    localStorage.clear();
    setUsername(null);
    setRole(null); // ✅ 清空角色
    navigate("/");
  };

  return (
    <div style={{ background: "#f5f5f5", padding: "0.5rem 1rem", display: "flex", justifyContent: "space-between" }}>
      <span>
        {username ? `👤 Welcome, ${username}` : "🔒 未登录"}
      </span>

      <div>
        {/* 👇 商户就不显示 Home */}
        {username && role !== "MERCHANT" && (
          <button onClick={() => navigate("/")}>🏠 Home</button>
        )}

        {username ? (
          <>
            <button
              onClick={() => {
                if (role === "MERCHANT") {
                  navigate("/merchant");
                } else {
                  navigate("/my-orders");
                }
              }}
              style={{ marginLeft: "1rem" }}
            >
              📦 {role === "MERCHANT" ? "Merchant Orders" : "My Orders"}
            </button>

            <button onClick={handleLogout} style={{ marginLeft: "1rem" }}>
              Logout
            </button>
          </>
        ) : (
          <>
            <button onClick={() => setAuthMode("login")} style={{ marginLeft: "1rem" }}>登录</button>
            <button onClick={() => setAuthMode("register")} style={{ marginLeft: "0.5rem" }}>注册</button>
          </>
        )}
      </div>

      {authMode && (
        <AuthModal
          mode={authMode}
          onClose={() => setAuthMode(null)}
          onLoginSuccess={(name) => {
            setUsername(name);
            setAuthMode(null);
          }}
        />
      )}
    </div>
  );
}
