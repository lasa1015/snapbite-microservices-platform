FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .
COPY src src

RUN ./mvnw clean package -DskipTests

CMD ["java", "-jar", "target/notification-service-0.0.1-SNAPSHOT.jar"]
