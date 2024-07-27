# Use the Eclipse Temurin 17 JDK image as the build stage
FROM eclipse-temurin:17-jdk AS build

# Set the working directory in the container
WORKDIR /app

# Copy the Maven wrapper files and the pom.xml
COPY .mvn/ .mvn
COPY mvnw .
COPY pom.xml .

# Install Maven dependencies
RUN ./mvnw dependency:go-offline

# Copy the rest of the application source code
COPY src ./src

# Package the application
RUN ./mvnw package -DskipTests

# Use the OpenJDK 17 JDK image as the run stage
FROM openjdk:17-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Create a group and user for Jenkins
RUN addgroup -S jenkins && adduser -S jenkins -G jenkins

# Install necessary packages
RUN apk update && apk add --no-cache openssh-client

# Copy the JAR file from the build stage to the run stage
COPY --from=build /app/target/employee-management-system-maven-0.0.1-SNAPSHOT.jar /employee-management-system-maven-0.0.1-SNAPSHOT.jar

# Expose the port the application will run on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/employee-management-system-maven-0.0.1-SNAPSHOT.jar"]
