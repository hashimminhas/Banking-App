# Build stage
FROM eclipse-temurin:21-jdk as build

WORKDIR /app

# Copy gradle wrapper and make it executable
COPY gradlew .
COPY gradlew.bat .
COPY gradle gradle
RUN chmod +x gradlew

# Copy gradle config
COPY build.gradle .
COPY settings.gradle .

# Download dependencies
RUN ./gradlew dependencies --no-daemon

# Copy source code
COPY src src

# Build the application
RUN ./gradlew clean build --no-daemon -x test

# Runtime stage
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the built jar from build stage
COPY --from=build /app/build/libs/green-day-bank-api-1.0.0.jar app.jar

# Expose the port the app runs on
EXPOSE 7070

# Run the application
CMD ["java", "-jar", "app.jar"]
