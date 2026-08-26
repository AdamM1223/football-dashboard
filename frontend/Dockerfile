# Stage 1: Build the React application with Node.js
FROM node:18-alpine AS build

WORKDIR /app

# Copy package descriptors first to optimize caching
COPY package*.json ./

RUN npm install

# Copy all project files and build the production bundle
COPY . .
RUN npm run build

# Stage 2: Serve the build output using lightweight Nginx
FROM nginx:alpine

COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]