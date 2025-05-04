import { useState } from "react";
import axios from "axios"; // 引入 axios 用于发送 HTTP 请求

type Props = {
  mode: "login" | "register";              // 弹窗模式：登录 或 注册
  onClose: () => void;                      // 关闭弹窗的回调函数
  onLoginSuccess: (username: string) => void; // 登录成功后触发的回调
};

// 登录 / 注册 弹窗组件
export default function AuthModal({ mode, onClose }: Props) {
  const { setUsername } = useUser();
  const [username, setLocalUsername] = useState("");
  const [password, setPassword] = useState("");
  const [email, setEmail] = useState("");
  const [role, setRole] = useState("USER");
  const [error, setError] = useState("");

  // 提交按钮逻辑（登录 or 注册）
  const handleSubmit = async () => {

    try {
      
      // 根据模式决定请求路径与提交字段
      const url = mode === "login" ? "/api/users/login" : "/api/users/register";

      const payload = mode === "login"
        ? { username, password }
        : { username, password, email, role };


      // 向后端发送 POST 请求。
      // 根据是否login了，决定请求的 URL 和 payload
      const res = await axios.post(url, payload);


      // 登录流程
      if (mode === "login") {

        const token = res.data.token;
        
        // localStorage存token
        localStorage.setItem("token", token); 


        // 登录成功后立即请求用户信息（/me 接口）
        const meRes = await axios.get("/api/users/me", {
          
          // 携带 token 发起请求
          headers: {
            Authorization: `Bearer ${token}` 
          },
        });

        // 从 /me 接口 获取的用户信息
        const user = meRes.data;
        
        // localstorage存用户名
        localStorage.setItem("username", user.username); 

        // 通知父组件：登录成功
        onLoginSuccess(user.username);                   
      } 
      
      else {

        // 注册成功：提示用户，关闭弹窗
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
        
        {/* 注册模式下额外填写邮箱和角色 */}
        {mode === "register" && (
          <>
            <input value={email} onChange={e => setEmail(e.target.value)} placeholder="邮箱" /><br />
            <select value={role} onChange={e => setRole(e.target.value)}>
              <option value="USER">普通用户</option>
              <option value="MERCHANT">商户</option>
            </select><br />
          </>
        )}

        {/* 错误提示 */}
        {error && <p style={{ color: "red" }}>{error}</p>}

        <button onClick={handleSubmit}>
          {mode === "login" ? "登录" : "注册"}
        </button>
        <button onClick={onClose}>取消</button>
      </div>
    </div>
  );
}
