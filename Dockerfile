# Dockerfile

# Use the correct base images
FROM eclipse-temurin:17-jdk AS build
FROM openjdk:17-jdk-alpine AS stage-1

# Create a user with a unique UID
RUN id -u 1000 &>/dev/null || useradd -m -u 1000 -s /bin/bash jenkin

# Install necessary packages
RUN apt-get update && apt-get install -y openssh-client

# Copy the built JAR file
COPY target/employee-management-system-maven-0.0.1-SNAPSHOT.jar /employee-management-system-maven-0.0.1-SNAPSHOT.jar

# Copy the JAR file to the final stage
COPY --from=build /employee-management-system-maven-0.0.1-SNAPSHOT.jar /employee-management-system-maven-0.0.1-SNAPSHOT.jar
