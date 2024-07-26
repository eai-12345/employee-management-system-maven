# Use eclipse-temurin base image
FROM eclipse-temurin:17-jdk AS build

# Create a user with a proper shell
RUN useradd -m -u 1000 -s /bin/bash jenkin

# Install necessary packages
RUN apt-get update && apt-get install -y openssh-client

# Copy the application JAR file
COPY target/employee-management-system-maven-0.0.1-SNAPSHOT.jar /employee-management-system-maven-0.0.1-SNAPSHOT.jar

# Switch to a smaller base image for the final build
FROM openjdk:17-jdk-alpine

# Copy the application JAR from the build stage
COPY --from=build /employee-management-system-maven-0.0.1-SNAPSHOT.jar /employee-management-system-maven-0.0.1-SNAPSHOT.jar

# Define the entry point for the container
ENTRYPOINT ["java", "-jar", "/employee-management-system-maven-0.0.1-SNAPSHOT.jar"]
