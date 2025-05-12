# 第一阶段：使用 Maven 编译项目
FROM maven:3.9.5-eclipse-temurin-21-alpine AS builder

WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# 第二阶段：使用精简的 JDK 镜像运行 jar
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
