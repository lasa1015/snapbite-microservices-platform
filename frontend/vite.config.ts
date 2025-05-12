import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';


export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080', // 统一转发到 Gateway
        changeOrigin: true,
        rewrite: path => path, // 不修改路径，让 Gateway 自己转发
      },
    },
  },
  build: {
    outDir: '../nginx/frontend-dist',
    emptyOutDir: true,
  },
});


// Vite 的 dev server 会自动拦截 /api/xxx 请求并转发到你指定的 Spring Boot 服务；

// // 多个后端服务（每个服务一个端口），Vite 代理自动分发
// export default defineConfig({
//   plugins: [react()],
//   server: {
//     proxy: {
//       '/api/users': { target: 'http://localhost:8081', changeOrigin: true },
//       '/api/restaurants': { target: 'http://localhost:8082', changeOrigin: true },
//       '/api/menu': { target: 'http://localhost:8082', changeOrigin: true },
//       '/api/cart': { target: 'http://localhost:8083', changeOrigin: true },
//       '/api/order': { target: 'http://localhost:8084', changeOrigin: true },
//     },
//   },
//   build: {
//     outDir: '../nginx/frontend-dist', // 直接打包进 nginx 目录
//     emptyOutDir: true, // 每次构建前清空目录（推荐）
//   },
// });


