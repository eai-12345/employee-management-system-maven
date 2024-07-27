# Build stage
FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

# Copy the Maven project files
COPY pom.xml .
COPY src ./src

# Package the application
RUN ./mvnw package -DskipTests

# Run stage
FROM openjdk:17-jdk-alpine

WORKDIR /app

# Create a user with a unique UID
RUN addgroup -S jenkins && adduser -S jenkins -G jenkins

# Install necessary packages
RUN apk update && apk add --no-cache openssh-client

# Copy the JAR file from the build stage
COPY --from=build /app/target/employee-management-system-maven-0.0.1-SNAPSHOT.jar /app/employee-management-system-maven-0.0.1-SNAPSHOT.jar

# Set the entry point for the container
ENTRYPOINT ["java", "-jar", "/app/employee-management-system-maven-0.0.1-SNAPSHOT.jar"]
