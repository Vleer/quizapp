# Trivia Quiz App

This is a simple project developed to demonstrate my programming abilities in building a trivia quiz application using React for the frontend and Spring Boot for the backend. The application fetches trivia questions from an external API, allows users to answer them, and then checks the submitted answers for correctness.

## Features

- Fetches trivia questions from an API and presents them to the user.
- Allows users to select answers to the questions.
- Submits user answers to the backend for validation and displays the correct answers afterward.
- Utilizes caching to improve performance by reducing redundant API requests.

## Skills Demonstrated

- React.js for building the interactive frontend.
- Spring Boot for creating the backend API.
- State management using React Hooks.
- Fetching data from an external API in React.
- Handling HTTP requests using Axios.
- Caching with Spring's `@Cacheable` and `@CacheEvict` annotations.
- Docker to containerize the application.
- Composing frontend and backend using Docker Compose.

## Running the Application

To run this project locally, you'll need Docker and Docker Compose installed.

### Quick Start

1. Clone this repository.
2. Open a terminal and navigate to the project directory.
3. Build the backend JAR file:

   ```bash
   ./build.sh
   ```

4. Start the application:

   ```bash
   docker compose up
   ```

The frontend will be accessible at http://localhost:3000, and the backend will be accessible at http://localhost:8080.

### Alternative: Manual Build

If you prefer to build manually:

```bash
# Build the backend JAR
cd quiz-backend
./mvnw clean package -DskipTests
cd ..

# Build and start Docker containers
docker compose up --build
```

### Manual Setup (Without Docker)

For the backend, navigate to the quiz-backend directory and run the Spring Boot application:

```bash
cd quiz-backend
./mvnw spring-boot:run
```

For the frontend, navigate to the quiz-frontend directory and run the following commands:

```bash
cd quiz-frontend
npm install
npm start
```

The frontend will be accessible at http://localhost:3000, and the backend at http://localhost:8080.

## Conclusion

This Trivia Quiz App is a demonstration of my skills in building full-stack web applications, showcasing proficiency in React, Spring Boot, API integration, and containerization using Docker. Feel free to explore the code to see how different components work together to create a functional trivia quiz game.
