# Use official Eclipse Temurin JDK for runtime
FROM eclipse-temurin:21-jre-jammy

# Set working directory
WORKDIR /app

# Copy JAR (assumes you already ran `mvn clean package -DskipTests` locally or in CI)
COPY Backend/target/*.jar app.jar

# Expose the port (Render maps it automatically)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-Xmx512m", "-Xms256m", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
