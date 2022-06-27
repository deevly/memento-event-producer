FROM appinair/jdk11-maven as base

FROM base as builder

WORKDIR /memento-event-producer
COPY . /memento-event-producer

RUN chmod 700 gradlew
RUN ./gradlew bootJar

FROM base as producer

COPY --from=builder /memento-event-producer/build/libs/memento-event-producer-0.0.1-SNAPSHOT.jar /producer.jar

ENTRYPOINT ["java","-jar","-Daws.key.access=${ACCESS_KEY}","-Daws.key.secret=${SECRET_KEY}", "/producer.jar"]