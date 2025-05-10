# 第一阶段：使用 Maven 镜像构建 .jar 包
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app

# 复制当前模块的所有文件到容器
COPY . .

# 构建 jar 包（跳过测试加快构建）
RUN mvn clean package -DskipTests

# 第二阶段：使用更轻量的运行环境
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# 拷贝构建出来的 jar 包
COPY --from=build /app/target/*.jar cart-service.jar

# 启动命令
ENTRYPOINT ["java", "-jar", "cart-service.jar"]
