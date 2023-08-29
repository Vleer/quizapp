# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-alpine as build-stage

# Set the working directory inside the container
WORKDIR /app

# Copy the compiled JAR file into the container
COPY target/quiz-backend-*.jar /app/app.jar

# Expose the port that your Spring Boot app runs on
EXPOSE 8080

# Run the Spring Boot app
CMD ["java", "-jar", "app.jar"]
