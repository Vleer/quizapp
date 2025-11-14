# Dockerfile for Spring Boot Backend
# This Dockerfile expects the JAR to be pre-built
# Run the build script first: ./build.sh
# Or build manually: cd quiz-backend && ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the pre-built JAR file
COPY target/*.jar app.jar

# Expose Spring Boot default port
EXPOSE 8080

# Start the application
ENTRYPOINT ["java", "-jar", "app.jar"]
