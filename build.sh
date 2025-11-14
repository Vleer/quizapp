#!/bin/bash

# Build script for the Quiz App
# This script builds the backend JAR before building Docker images

set -e

echo "Building backend JAR..."
cd quiz-backend
chmod +x mvnw
./mvnw clean package -DskipTests
cd ..

echo "Backend JAR built successfully!"
echo ""
echo "Now you can:"
echo "  - Build Docker images: docker compose build"
echo "  - Or start the application directly: docker compose up"
