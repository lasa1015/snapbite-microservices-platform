#!/bin/bash
set -e

echo "🔧 更新系统..."
sudo apt update -y && sudo apt upgrade -y

echo "🔧 安装基础依赖..."
sudo apt install -y ca-certificates curl gnupg lsb-release zip unzip git postgresql-client mysql-client

echo "🔐 配置 Docker GPG 密钥..."
sudo install -m 0755 -d /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg

echo "📦 添加 Docker 仓库..."
echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

echo "🔄 更新 apt..."
sudo apt update -y

echo "🐳 安装 Docker & Compose..."
sudo apt install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

echo "🚀 启动 Docker 服务..."
sudo systemctl enable docker
sudo systemctl start docker
sudo usermod -aG docker $USER

echo "🔐 登录 Docker Hub（请手动输入用户名密码）"
docker login

echo "✅ 初始化完成！"
echo "📌 请重新登录 SSH 会话以生效 Docker 权限（Docker 命令无需 sudo）"
echo "📦 你可以使用以下命令连接数据库："
echo "   👉 PostgreSQL: psql -h <RDS地址> -U <用户名> -d <数据库名> -p 5432"
echo "   👉 MySQL:      mysql -h <RDS地址> -u <用户名> -p"
