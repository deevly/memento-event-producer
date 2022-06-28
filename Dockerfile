FROM openjdk:11-jdk-oracle AS builder

WORKDIR /producer
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY src src
RUN chmod +x ./gradlew
RUN ./gradlew bootJAR

FROM openjdk:11-jdk-oracle
COPY --from=builder /producer/build/libs/*.jar /producer.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","-Daws.key.access=${ACCESS_KEY}","-Daws.key.secret=${SECRET_KEY}", "/producer.jar"]