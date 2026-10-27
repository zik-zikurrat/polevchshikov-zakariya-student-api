# ============ Stage 1: build the jar with Gradle ============
FROM eclipse-temurin:21-jdk AS build
WORKDIR /workspace

# Copy the Gradle wrapper and build files first: this layer (and the downloaded
# dependencies) is cached and reused while only the source code changes
COPY gradlew settings.gradle build.gradle ./
COPY gradle ./gradle
RUN chmod +x gradlew && ./gradlew --no-daemon dependencies > /dev/null

COPY src ./src
# Tests are executed by the Jenkins "Test" stage, so the image build only packages the jar
RUN ./gradlew --no-daemon bootJar -x test

# ============ Stage 2: small runtime image (JRE only) ============
FROM eclipse-temurin:21-jre
LABEL org.opencontainers.image.title="polevchshikov-zakariya-student-api" \
      org.opencontainers.image.authors="Polevchshikov Zakariya (IT2-2312, 37052)"
WORKDIR /app

# Run the application as a non-root user
RUN useradd --system --create-home appuser
COPY --from=build /workspace/build/libs/*.jar app.jar
USER appuser

# Port configured in application.properties (server.port=8080)
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
