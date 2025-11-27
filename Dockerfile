# Use a lightweight Java runtime
FROM openjdk:17-jdk-slim

# Set working directory inside the container
WORKDIR /app

# Copy your JAR file into the container
COPY target/interestCalculator-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your app uses
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
