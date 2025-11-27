# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the Maven wrapper and project files
COPY mvnw pom.xml ./
COPY .mvn .mvn

# Copy the source code
COPY src src

# Make the Maven wrapper executable
RUN chmod +x mvnw

# Build the Spring Boot application
RUN ./mvnw clean package -DskipTests

# Copy the jar to the root of the container
RUN cp target/*.jar app.jar

# Set the startup command
CMD ["java", "-jar", "app.jar"]
