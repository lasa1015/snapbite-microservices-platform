# 第一阶段：使用包含 JDK 21 的 Maven 镜像来构建项目（适合构建 Spring Boot 应用）
FROM maven:3.9.4-eclipse-temurin-21 AS build

# 设置工作目录，所有命令都在 /app 目录中执行
WORKDIR /app

# 将当前目录下所有文件复制到镜像中的 /app 目录
COPY . .

# 使用 Maven 打包项目，跳过测试（否则可能报错或拖慢构建速度）
RUN mvn clean package -DskipTests
# ↑ 打包后会在 target/ 目录下生成一个 xxx.jar 文件（Spring Boot 可执行 Jar 包）

# -----------------------------
# 第二阶段：使用轻量级的 JRE 镜像运行打包好的应用
# -----------------------------

# 使用 Eclipse Temurin 发布的 Alpine 版本 JDK 21，只包含运行时环境（体积小）
FROM eclipse-temurin:21-jre-alpine

# 设置运行时工作目录为 /app
WORKDIR /app

# 从上一个阶段的镜像中，复制构建好的 jar 包过来（*通配符支持任意名称 jar）
COPY --from=build /app/target/*.jar user-service.jar

# 设置容器启动时默认执行的命令，运行 Spring Boot 应用
ENTRYPOINT ["java", "-jar", "user-service.jar"]


