import { useState } from "react";
import { Eye, EyeOff, X } from "lucide-react";
import { useUserStore } from "../stores/userStore";
import { useAuth } from "../hooks/useAuth";
import { useCartSync } from "../hooks/useCartSync"; // 
import { useToast } from "../components/Toast"; // ✅ 新增
import { Role } from "../types/user";

type Props = {
  mode: "login" | "register";
  onClose: () => void;
  onLoginSuccess: (username: string, role: Role) => void; // ✅ 正确类型
  setMode: (mode: "login" | "register") => void;
};



export default function AuthModal({ mode, onClose, onLoginSuccess, setMode }: Props) {
  const { setUsername, setRole } = useUserStore();
  const { login, register } = useAuth();

  const [username, setLocalUsername] = useState("");
  const [password, setPassword] = useState("");
  const [email, setEmail] = useState("");
  const [role, setLocalRole] = useState("USER");
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState("");
  const showToast = useToast();



   const { syncCart } = useCartSync();

  const handleSubmit = async () => {
  try {
    if (mode === "login") {
      const user = await login(username, password);
      setUsername(user.username);
      setRole(user.role);

      const token = localStorage.getItem("token");
      if (token) {
        await syncCart(token);
      }

      // ✅ 把 role 一起传出去
      onLoginSuccess(user.username, user.role);

    } else {
      await register({ username, password, email, role: role as "USER" | "MERCHANT" });


      showToast("Registered successfully. Please log in.");
      setMode("login");
    }
  } catch (err: any) {
    const msg =
      err.response?.data?.message ||
      (typeof err.response?.data === "string" ? err.response.data : "") ||
      "请求失败";
    setError(msg);
  }
};


  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white rounded-[20px] p-8 w-[360px] relative shadow-lg">

        {/* 关闭按钮 */}
        <button
          onClick={onClose}
          className="absolute top-3 right-3 text-gray-400 hover:text-black text-xl"
        >
          <X />
        </button>

        {/* 标题 */}
        <h2 className="text-2xl font-[500] text-gray-900 mb-6 font-outfit">
          {mode === "login" ? "Login" : "Register"}
        </h2>

        {/* 用户名 */}
        <input
          value={username}
          onChange={(e) => setLocalUsername(e.target.value)}
          placeholder="Username *"
          className="w-full border border-gray-300 rounded-[4px] px-4 py-2 mb-6 focus:outline-none focus:ring-2 focus:ring-red-300 text-sm text-gray-900"
        />

        {/* 密码 */}
        <div className="relative mb-6">
          <input
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="Password *"
            type={showPassword ? "text" : "password"}
            className="w-full border border-gray-300 rounded-[4px] px-4 py-2 pr-10 focus:outline-none focus:ring-2 focus:ring-red-300 text-sm text-gray-900"
          />
          <button
            onClick={() => setShowPassword((v) => !v)}
            className="absolute right-2 top-2.5 text-red-400 hover:text-red-500"
            type="button"
          >
            {showPassword ? <EyeOff size={20} /> : <Eye size={20} />}
          </button>
        </div>

        {/* 注册专属字段 */}
        {mode === "register" && (
          <>
            <input
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="Email *"
              className="w-full border border-gray-300 text-gray-700 rounded-[4px] px-4 py-2 mb-6 text-sm"
            />
            <select
              value={role}
              onChange={(e) => setLocalRole(e.target.value)}
              className="w-full text-gray-700 border border-gray-300 rounded-[4px] px-4 py-2 mb-6 text-sm"
            >
           <option value="USER">User</option>
<option value="MERCHANT">Merchant</option>
            </select>
          </>
        )}

        {/* 错误信息 */}
        {error && <p className="text-red-500 text-sm mb-4">{error}</p>}

        {/* 提交按钮 */}
        <button
          onClick={handleSubmit}
          className="w-full bg-primary hover:bg-red-700 text-white font-[500] font-outfit py-2 rounded-[10px] transition"
        >
          {mode === "login" ? "LOGIN" : "SIGN UP"}
        </button>

        {/* 底部切换 */}
        <p className="text-center text-sm text-gray-600 mt-6">
          {mode === "login" ? "New Member?" : "Already have an account?"}{" "}
          <button
            className="text-primary  hover:underline"
            onClick={() => setMode(mode === "login" ? "register" : "login")}
          >
            {mode === "login" ? "SIGN UP" : "LOGIN"}
          </button>
        </p>
      </div>
    </div>
  );
}
