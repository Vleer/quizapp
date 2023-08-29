# Use the official OpenJDK image as the base image
FROM openjdk:11-jre-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the compiled JAR file and the pom.xml into the container
COPY quiz-backend/target/quiz-backend-*.jar /app/app.jar
COPY quiz-backend/pom.xml /app/pom.xml

# Expose the port that your Spring Boot app runs on
EXPOSE 8080

# Run the Spring Boot app
CMD ["java", "-jar", "app.jar"]
