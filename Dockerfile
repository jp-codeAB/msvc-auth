FROM gradle:8.10.2-jdk21 AS builder
WORKDIR /app

COPY build.gradle settings.gradle ./
COPY gradle gradle
COPY src src

RUN gradle clean build -x test --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080
HEALTHCHECK CMD curl -f http://localhost:8080/actuator/health || exit 1

ENV SPRING_PROFILES_ACTIVE=dev \
    CONFIG_SERVER_URI=http://config-server:8888 \
    EUREKA_URI=http://eureka-server:8761/eureka/ \
    DB_HOST=postgres \
    DB_PORT=5432 \
    DB_NAME=authdb \
    DB_USER=postgres \
    DB_PASSWORD=100juanU \
    JWT_SECRET=VvQ4uF5t+HhX0fG6oA9/eJ7hR5t7yL1D3kG9QxT4zC2o= \
    TZ=America/Bogota

ENTRYPOINT ["java", "-jar", "app.jar"]

