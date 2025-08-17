# Use OpenJDK 17 as base image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY mvnw .
COPY mvnw.cmd .
COPY pom.xml .
COPY .mvn .mvn

# Download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# Expose port
EXPOSE 8081

# Set environment variables for production
ENV SPRING_PROFILES_ACTIVE=production
ENV SERVER_PORT=8081

# Run the application
CMD ["java", "-jar", "target/diepxdemo-0.0.1-SNAPSHOT.jar"]
