#!/bin/bash

# Build script for the Quiz App
# This script builds the backend JAR before building Docker images

set -e

echo "Building backend JAR..."
cd quiz-backend
chmod +x mvnw
./mvnw clean package -DskipTests
cd ..

echo "Building Docker images..."
docker compose build

echo "Build complete! Run 'docker compose up' to start the application."
