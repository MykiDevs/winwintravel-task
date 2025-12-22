FROM maven:3.8.4-openjdk-17-slim AS builder
WORKDIR /app
COPY .mvn .mvn
COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src src

RUN mvn package -DskipTests

FROM eclipse-temurin:17-jre-jammy

COPY --from=builder /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]