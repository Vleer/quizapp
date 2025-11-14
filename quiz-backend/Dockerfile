# Use an official OpenJDK runtime as a parent image
# Build stage: build the Spring Boot app with Maven
FROM maven:3.8.8-openjdk-17-slim AS build
WORKDIR /build

# Copy only Maven files first to leverage layer caching
COPY pom.xml mvnw ./
COPY .mvn .mvn

# Copy source code
COPY src ./src

# Build the project (skip tests to keep build fast in CI/dev)
RUN mvn -B -DskipTests package

# Runtime stage: use a small JRE image
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copy the fat jar from the build stage
COPY --from=build /build/target/*.jar /app/app.jar

# Expose Spring Boot default port
EXPOSE 8080

# Start the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
