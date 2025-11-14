# Use an official Node runtime as the base image
FROM node:18 as build-stage

# Set the working directory inside the container
WORKDIR /app

# Copy package.json and package-lock.json to the working directory
COPY package*.json ./

# Install project dependencies
RUN npm install

# Copy the rest of the application code to the working directory
COPY . .

# Build the React app
RUN npm run build

# Use a lightweight Node runtime for serving the production build
FROM node:18-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built React app from the previous stage
COPY --from=build-stage /app/build .

# Install a simple HTTP server to serve the static content
RUN npm install -g serve

# Expose port 3000
EXPOSE 3000

# Command to start the server
CMD ["serve", "-p", "3000", "-s", "."]
