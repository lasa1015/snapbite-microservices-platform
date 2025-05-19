#!/bin/bash

DOCKER_USERNAME="lasa1015"

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

for IMAGE in "${IMAGES[@]}"; do
  echo "🚀 Pulling image: ${DOCKER_USERNAME}/${IMAGE}:latest"
  docker pull ${DOCKER_USERNAME}/${IMAGE}:latest
done

echo "✅ All images pulled successfully."
