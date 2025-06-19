# Stage 1: build
FROM gradle:7.6-jdk17 AS builder
WORKDIR /app
COPY --chown=gradle:gradle . .
RUN gradle :crawl-app:bootJar --no-daemon

# Stage 2: runtime
FROM openjdk:21-jdk-slim
RUN apt-get update && apt-get install -y curl iputils-ping
WORKDIR /app
COPY --from=builder /app/crawl-app/build/libs/app.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]