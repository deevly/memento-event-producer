FROM openjdk:11.0.15-jre-slim AS base

FROM base as builder

WORKDIR /producer
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY src src
RUN chmod +x ./gradlew
RUN ./gradlew bootJAR

FROM base as producer

COPY --from=builder /producer/build/libs/*.jar /producer.jar
EXPOSE 9180
ENTRYPOINT ["java","-jar","-Daws.key.access=${ACCESS_KEY}","-Daws.key.secret=${SECRET_KEY}", "/producer.jar"]
