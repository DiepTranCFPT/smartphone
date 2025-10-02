#!/bin/bash

# Install Java 17
curl -sL https://get.sdkman.io | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk install java 17.0.8-amzn

# Make the Maven wrapper executable
chmod +x mvnw

# Build the application
./mvnw clean package -DskipTests

# Create the deployment directory structure
mkdir -p .vercel/output/static

# Copy the JAR file to the output directory
cp target/*.jar .vercel/output/static/
