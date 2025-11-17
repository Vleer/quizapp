FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

# Install dos2unix to handle line endings
RUN apk add --no-cache dos2unix

# Copy Maven wrapper and configuration
COPY .mvn .mvn
COPY mvnw mvnw.cmd pom.xml ./

# Fix line endings and make executable
RUN dos2unix mvnw && chmod +x mvnw

# Copy source code and build the application
COPY src src
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
