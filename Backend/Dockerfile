# Use Maven builder image
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
# Copy only the POM file first for better layer caching
COPY Backend/pom.xml .
# Download dependencies
RUN mvn dependency:go-offline -B
# Copy source code
COPY Backend/src/ ./src/
# Build the application
RUN mvn clean package -DskipTests

# Use JRE image for final image
FROM eclipse-temurin:21-jre
WORKDIR /app
# Copy the JAR file from the build stage
COPY --from=build /app/target/MoneyMate-0.0.1-SNAPSHOT.jar app.jar
# Expose the port the app runs on
EXPOSE 8080
# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]