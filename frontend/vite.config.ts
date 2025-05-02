import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'


// 浏览器原本会拦截你从 5173 发请求到 8080 的行为（CORS 问题）；
// 但 Vite 的 dev server 会把 /api/xxx 请求直接转发给你的后端（绕过浏览器）；

export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080', // 你的 Spring Boot 后端地址
        changeOrigin: true,
      },
    },
  },
})

