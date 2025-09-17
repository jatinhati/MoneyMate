# Build stage
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace/app

# Copy build files
COPY Backend/pom.xml .
COPY Backend/src/ src/

# Build the application
RUN mvn clean package -DskipTests

# Production stage
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Copy the JAR from the build stage
COPY --from=build /workspace/app/target/*.jar app.jar

# Set the timezone
ENV TZ=Asia/Kolkata

# Set JVM options for production
ENV JAVA_OPTS="-Xmx512m -Xms256m -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=prod"

# Expose the port the app runs on
EXPOSE 8080

# Command to run the application
ENTRYPOINT exec java $JAVA_OPTS -jar app.jar