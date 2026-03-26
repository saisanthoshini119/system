# Multi-stage build for the Spring Boot app

# 1) Build stage
FROM maven:3.9.5-eclipse-temurin-17 AS build
WORKDIR /app

# Copy project files
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn -B -DskipTests package

# 2) Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the fat jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port
EXPOSE 10000

# Run application
ENTRYPOINT ["java", "-jar", "app.jar"]
