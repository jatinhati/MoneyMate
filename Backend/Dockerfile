# Use official Eclipse Temurin JDK for runtime
FROM eclipse-temurin:21-jre-jammy

# Set working directory
WORKDIR /app

# Copy JAR (assumes you already ran `mvn clean package -DskipTests` locally or in CI)
<<<<<<< HEAD
COPY Backend/target/MoneyMate-0.0.1-SNAPSHOT.jar app.jar
=======
COPY Backend/target/*.jar app.jar
>>>>>>> f4e2f1624e499851ff5347651ccbb8fbb23ebfb0

# Expose the port (Render maps it automatically)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-Xmx512m", "-Xms256m", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
