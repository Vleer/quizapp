# Dockerfile for Spring Boot Backend
# This Dockerfile expects the JAR to be pre-built
# Build the JAR first with: ./mvnw clean package -DskipTests
# Or use the build.sh script in the root directory

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copy the pre-built JAR file
COPY target/*.jar /app/app.jar

# Expose Spring Boot default port
EXPOSE 8080

# Start the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
