import { useState } from "react";

type Props = {
  mode: "login" | "register";
  onClose: () => void;
  onLoginSuccess: (username: string) => void;
};

export default function AuthModal({ mode, onClose, onLoginSuccess }: Props) {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [email, setEmail] = useState("");
  const [role, setRole] = useState("USER");
  const [error, setError] = useState("");

  const handleSubmit = async () => {
    try {
      const url = mode === "login" ? "/api/users/login" : "/api/users/register";
      const payload =
        mode === "login"
          ? { username, password }
          : { username, password, email, role };

      const res = await fetch(url, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });

      if (!res.ok) {
        const text = await res.text();
        throw new Error(text);
      }

      const data = await res.json();

      if (mode === "login") {
        localStorage.setItem("token", data.token);
        const meRes = await fetch("/api/users/me", {
          headers: { Authorization: `Bearer ${data.token}` },
        });
        const user = await meRes.json();
        localStorage.setItem("username", user.username);
        onLoginSuccess(user.username);
      } else {
        alert("注册成功，请登录");
        onClose();
      }
    } catch (err: any) {
      setError(err.message || "请求失败");
    }
  };

  return (
    <div style={{
      position: "fixed", top: 0, left: 0, width: "100vw", height: "100vh",
      backgroundColor: "rgba(0,0,0,0.5)", display: "flex",
      justifyContent: "center", alignItems: "center", zIndex: 1000
    }}>
      <div style={{ backgroundColor: "white", padding: "2rem", borderRadius: "10px", width: "300px" }}>
        <h2>{mode === "login" ? "登录" : "注册"}</h2>

        <input value={username} onChange={e => setUsername(e.target.value)} placeholder="用户名" /><br />
        <input value={password} onChange={e => setPassword(e.target.value)} placeholder="密码" type="password" /><br />
        {mode === "register" && (
          <>
            <input value={email} onChange={e => setEmail(e.target.value)} placeholder="邮箱" /><br />
            <select value={role} onChange={e => setRole(e.target.value)}>
              <option value="USER">普通用户</option>
              <option value="MERCHANT">商户</option>
            </select><br />
          </>
        )}

        {error && <p style={{ color: "red" }}>{error}</p>}
        <button onClick={handleSubmit}>{mode === "login" ? "登录" : "注册"}</button>
        <button onClick={onClose}>取消</button>
      </div>
    </div>
  );
}
