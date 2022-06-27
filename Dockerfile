FROM appinair/jdk11-maven

COPY build/libs/memento-event-producer-0.0.1-SNAPSHOT.jar producer.jar

ENTRYPOINT ["java","-jar","-Daws.key.access=${ACCESS_KEY}","-Daws.key.secret=${SECRET_KEY}", "/producer.jar"]