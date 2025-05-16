#!/bin/bash

set -e

echo "📦 创建 4GB 的 Swap 文件..."
sudo fallocate -l 4G /swapfile || sudo dd if=/dev/zero of=/swapfile bs=1M count=4096

echo "🔒 设置权限..."
sudo chmod 600 /swapfile

echo "🔧 格式化为 swap 区..."
sudo mkswap /swapfile

echo "🚀 启用 swap..."
sudo swapon /swapfile

echo "📌 持久化配置到 /etc/fstab..."
grep -q "/swapfile" /etc/fstab || echo '/swapfile none swap sw 0 0' | sudo tee -a /etc/fstab

echo "✅ 完成！当前内存和 Swap 状态如下："
free -h
