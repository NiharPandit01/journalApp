# Stage 1: Build
FROM eclipse-temurin:17-jdk-jammy AS build
COPY . .
RUN ./mvnw clean package -DskipTests

# Stage 2: Final Run
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY --from=build /target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]