FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .
COPY src src

# 本地用的是 Windows 系统，mvnw 文件默认是没有执行权限的
# 构建发生在 Linux 环境中（比如 GitHub Actions 或 Docker 容器），就会导致权限错误
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

CMD ["java", "-jar", "target/notification-service-0.0.1-SNAPSHOT.jar"]
