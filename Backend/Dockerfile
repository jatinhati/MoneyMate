# Use Maven builder image
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY Backend/pom.xml .
COPY Backend/src/ ./src/
RUN mvn clean package -DskipTests

# Use JRE image for final image
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/MoneyMate-0.0.1-SNAPSHOT.jar MoneyMate-v1.0.jar
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "MoneyMate-v1.0.jar"]