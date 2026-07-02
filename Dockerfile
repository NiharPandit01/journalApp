# 1. Use an official OpenJDK 17 runtime
FROM eclipse-temurin:17-jdk-jammy

# 2. Create a directory inside the container
WORKDIR /app

# 3. Copy the built JAR file from your target folder to the container
COPY target/*.jar app.jar

# 4. Expose the port your Spring Boot app runs on
EXPOSE 8081

# 5. Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]