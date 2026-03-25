# Multi-stage build for the Spring Boot intern-backend app

# 1) Build stage
FROM maven:3.9.5-eclipse-temurin-17 AS build
WORKDIR /app

# Copy only the files needed for dependency resolution first (to use Docker cache)
COPY pom.xml .
COPY src ./src

# Build the app (skip tests in build stage; run tests in CI if needed)
RUN mvn -B -DskipTests package

# 2) Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the fat jar from the build stage
COPY --from=build /app/target/intern-backend-0.0.1-SNAPSHOT.jar ./app.jar

# Expose the port the app is configured to use
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]