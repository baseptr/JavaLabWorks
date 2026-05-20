FROM eclipse-temurin:25-jdk AS builder
WORKDIR /app
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY Task_4_Docker/build.gradle Task_4_Docker/build.gradle
COPY Task_4_Docker/src Task_4_Docker/src
RUN chmod +x gradlew && ./gradlew :Task_4_Docker:bootJar --no-daemon

FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=builder /app/Task_4_Docker/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
