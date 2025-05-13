#!/bin/bash

# 写死用户名
DOCKER_USERNAME="lasa1015"

# 镜像名列表
IMAGES=(
  snapbite-user-service
  snapbite-restaurant-service
  snapbite-cart-service
  snapbite-order-service
  snapbite-nginx
  snapbite-eureka
  snapbite-gateway
  snapbite-mongo
  snapbite-notification-service
)

# 拉取镜像
for IMAGE in "${IMAGES[@]}"; do
  echo "🚀 Pulling image: ${DOCKER_USERNAME}/${IMAGE}:latest"
  docker pull ${DOCKER_USERNAME}/${IMAGE}:latest
done

echo "✅ All images pulled successfully."
