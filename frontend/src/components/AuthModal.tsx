import { useState } from "react";
import { useUser } from "../context/UserContext"; // 引入全局用户上下文
import { useAuth } from "../hooks/useAuth"; // 引入封装好的登录/注册逻辑

type Props = {
  mode: "login" | "register";
  onClose: () => void;
  onLoginSuccess: (username: string) => void;
};

export default function AuthModal({ mode, onClose, onLoginSuccess }: Props) {
  const { setUsername } = useUser();
  const { login, register } = useAuth(); // 调用封装好的认证逻辑

  const [username, setLocalUsername] = useState("");
  const [password, setPassword] = useState("");
  const [email, setEmail] = useState("");
  const [role, setRole] = useState("USER");
  const [error, setError] = useState("");

  const handleSubmit = async () => {
    try {
      if (mode === "login") {
        const user = await login(username, password);
        setUsername(user.username);
        onLoginSuccess(user.username);
        
        // ⬇️ 角色判断跳转
        if (user.role === "MERCHANT") {
          window.location.href = "/merchant"; // 或使用 navigate("/merchant")
        }
      } else {
        await register({ username, password, email, role });
        alert("注册成功，请登录");
        onClose();
      }
    } catch (err: any) {
      setError(err.response?.data || "请求失败");
    }
  };
  



  return (
    <div style={{
      position: "fixed", top: 0, left: 0, width: "100vw", height: "100vh",
      backgroundColor: "rgba(0,0,0,0.5)", display: "flex",
      justifyContent: "center", alignItems: "center", zIndex: 1000
    }}>
      <div style={{
        backgroundColor: "white", padding: "2rem",
        borderRadius: "10px", width: "300px"
      }}>
        <h2>{mode === "login" ? "登录" : "注册"}</h2>
        <input value={username} onChange={e => setLocalUsername(e.target.value)} placeholder="用户名" /><br />
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

        <button onClick={handleSubmit}>
          {mode === "login" ? "登录" : "注册"}
        </button>
        <button onClick={onClose}>取消</button>
      </div>
    </div>
  );
}
