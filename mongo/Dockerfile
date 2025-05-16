FROM mongo:8



#把整个 mongo-init/ 文件夹复制到镜像中 MongoDB 自动执行目录下。
COPY mongo-init /docker-entrypoint-initdb.d/
