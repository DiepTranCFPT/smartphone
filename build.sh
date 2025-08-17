#!/bin/bash

# Build script for Vercel deployment
echo "Starting build process..."

# Install Java 17 if not available
if ! command -v java &> /dev/null; then
    echo "Installing Java 17..."
    apt-get update
    apt-get install -y openjdk-17-jdk
fi

# Make mvnw executable
chmod +x ./mvnw

# Clean and build the project
echo "Building Spring Boot application..."
./mvnw clean package -DskipTests

# Create build output directory
mkdir -p dist

# Copy the jar file to dist directory
cp target/*.jar dist/

echo "Build completed successfully!"
