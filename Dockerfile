FROM gradle:8.10.2-jdk21 AS builder
WORKDIR /app

COPY build.gradle settings.gradle ./
COPY gradle gradle

RUN gradle dependencies --no-daemon || true

COPY src src

RUN gradle clean build -x test --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=dev \
    TZ=America/Bogota

ENTRYPOINT ["java", "-jar", "app.jar"]
