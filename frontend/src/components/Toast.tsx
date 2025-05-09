import { createContext, useContext, useState, useEffect } from "react";
import clsx from "clsx"; // 如未安装：npm i clsx

const ToastContext = createContext<(msg: string) => void>(() => {});

export function ToastProvider({ children }: { children: React.ReactNode }) {
  const [message, setMessage] = useState<string | null>(null);
  const [visible, setVisible] = useState(false);

  const showToast = (msg: string) => {
    setMessage(msg);
    setVisible(true);
  };

  useEffect(() => {
    if (message) {
      // 先显示
      const fadeOutTimer = setTimeout(() => setVisible(false), 800);
      // 再移除内容
      const clearTimer = setTimeout(() => setMessage(null), 2000);
      return () => {
        clearTimeout(fadeOutTimer);
        clearTimeout(clearTimer);
      };
    }
  }, [message]);

  return (
    <ToastContext.Provider value={showToast}>
      {children}

      {message && (
        <div
          className={clsx(
            "fixed bottom-8 right-8 bg-primary text-white px-12 py-6 rounded-2xl shadow-xl font-outfit text-lg font-semibold z-50 transition-opacity duration-500",
            visible ? "opacity-100" : "opacity-0"
          )}
        >
          {message}
        </div>
      )}
    </ToastContext.Provider>
  );
}

export function useToast() {
  return useContext(ToastContext);
}
