#!/bin/bash
set -e

echo "📥 安装 k0s 二进制..."
curl -sSLf https://get.k0s.sh | sudo sh

echo "🛠 安装为 controller + worker 模式..."
sudo k0s install controller --enable-worker

echo "🚀 启动 k0s 服务..."
sudo k0s start

echo "🔐 配置 kubectl"
sleep 10
sudo k0s kubeconfig admin > ~/.kube/config
chmod 600 ~/.kube/config

echo "✅ 查看节点状态："
kubectl get nodes -o wide

