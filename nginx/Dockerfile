FROM nginx:latest

# 拷贝构建好的前端资源到 nginx 静态目录
COPY frontend-dist /usr/share/nginx/html/

# 拷贝自定义 nginx 配置
COPY default.conf /etc/nginx/conf.d/default.conf
