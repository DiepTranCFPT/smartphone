#!/bin/bash

# Make the mvnw script executable
chmod +x mvnw

# Run Maven build
./mvnw clean package -DskipTests
