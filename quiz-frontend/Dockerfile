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

# Build the React app (homepage: "." in package.json makes paths relative)
RUN npm run build

# Use nginx to serve the static files
FROM nginx:alpine

# Copy the built React app to nginx
COPY --from=build-stage /app/build /usr/share/nginx/html

# Copy nginx configuration
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Expose port 3000
EXPOSE 3000

# Start nginx
CMD ["nginx", "-g", "daemon off;"]
