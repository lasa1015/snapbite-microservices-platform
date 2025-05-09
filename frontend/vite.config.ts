import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// Vite 的 dev server 会自动拦截 /api/xxx 请求并转发到你指定的 Spring Boot 服务；

// 多个后端服务（每个服务一个端口），Vite 代理自动分发
export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/api/users': {
        target: 'http://localhost:8081', // user-service
        changeOrigin: true,
      },
      '/api/restaurants': {
        target: 'http://localhost:8082', // restaurant-service
        changeOrigin: true,
      },
      '/api/menu': {
        target: 'http://localhost:8082', // 也是 restaurant-service
        changeOrigin: true,
      },
      '/api/cart': {
        target: 'http://localhost:8083', // cart-service
        changeOrigin: true,
      },
      '/api/order': {
        target: 'http://localhost:8084', // order-service
        changeOrigin: true,
      },
    },
  },
});
