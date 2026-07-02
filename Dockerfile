# Stage 1: Build the app
FROM eclipse-temurin:17-jdk-jammy AS build
COPY . .
# Add execute permissions to the Maven wrapper
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Stage 2: Final Run
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
# Copy the built JAR from the build stage (adjusting for the standard target location)
COPY --from=build /target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]