# Use a working OpenJDK 17 image
FROM openjdk:17.0.8-jdk-bullseye

WORKDIR /app

# Copy Maven wrapper and project files
COPY mvnw pom.xml ./
COPY .mvn .mvn

# Copy source code
COPY src src

# Make Maven wrapper executable
RUN chmod +x mvnw

# Build Spring Boot jar
RUN ./mvnw clean package -DskipTests

# Copy the built jar
RUN cp target/*.jar app.jar

# Expose port for Render
EXPOSE 8080

# Start the Spring Boot app
CMD ["java", "-jar", "app.jar"]
