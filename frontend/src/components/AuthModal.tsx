import { useState } from "react";


import { useUserStore } from "../stores/userStore";
import { useAuth } from "../hooks/useAuth";

type Props = {
  mode: "login" | "register";
  onClose: () => void;
  onLoginSuccess: (username: string) => void;
};

export default function AuthModal({ mode, onClose, onLoginSuccess }: Props) {
  const { setUsername, setRole } = useUserStore(); // ✅ 替换 useUser
  const { login, register } = useAuth();

  const [username, setLocalUsername] = useState("");
  const [password, setPassword] = useState("");
  const [email, setEmail] = useState("");
  const [role, setLocalRole] = useState("USER");
  const [error, setError] = useState("");

  const handleSubmit = async () => {
    try {
      if (mode === "login") {
        const user = await login(username, password);
        setUsername(user.username);
        setRole(user.role);
        onLoginSuccess(user.username);

        if (user.role === "MERCHANT") {
          window.location.href = "/merchant";
        }
      } else {
        await register({ username, password, email, role: role });
        alert("注册成功，请登录");
        onClose();
      }
    } catch (err: any) {
      // ✅ 修复关键错误：防止把对象直接渲染成 React child
      const msg =
        err.response?.data?.message ||
        (typeof err.response?.data === "string" ? err.response.data : "") ||
        "请求失败";
      setError(msg);
    }
  };

  return (
    <div
      style={{
        position: "fixed",
        top: 0,
        left: 0,
        width: "100vw",
        height: "100vh",
        backgroundColor: "rgba(0,0,0,0.5)",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        zIndex: 1000,
      }}
    >
      <div
        style={{
          backgroundColor: "white",
          padding: "2rem",
          borderRadius: "10px",
          width: "300px",
        }}
      >
        <h2>{mode === "login" ? "登录" : "注册"}</h2>
        <input
          value={username}
          onChange={(e) => setLocalUsername(e.target.value)}
          placeholder="用户名"
        />
        <br />
        <input
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          placeholder="密码"
          type="password"
        />
        <br />

        {mode === "register" && (
          <>
            <input
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="邮箱"
            />
            <br />
            <select value={role} onChange={(e) => setLocalRole(e.target.value)}>
              <option value="USER">普通用户</option>
              <option value="MERCHANT">商户</option>
            </select>
            <br />
          </>
        )}

        {/* ✅ 错误提示文字修复 */}
        {error && (
          <p style={{ color: "red", marginTop: "0.5rem" }}>{error}</p>
        )}

        <button onClick={handleSubmit}>
          {mode === "login" ? "登录" : "注册"}
        </button>
        <button onClick={onClose} style={{ marginLeft: "0.5rem" }}>
          取消
        </button>
      </div>
    </div>
  );
}
