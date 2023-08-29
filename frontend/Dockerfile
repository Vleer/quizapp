# Use the official Node.js image as the base image
FROM node:14

# Set the working directory inside the container
WORKDIR /app

# Copy package.json and package-lock.json into the container
COPY quiz-frontend/package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the application code into the container
COPY quiz-frontend/ ./

# Build the React app
RUN npm run build

# Expose the port that the React app runs on
EXPOSE 3000

# Start the React app
CMD ["npm", "start"]
