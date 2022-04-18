# memento-event-producer
memento Chrome extension의 이벤트 수신 및 Kafka 이벤트 생성기

## Kafka 구성

### EC2 셋업

EC2 3대 기동

- 로컬에서 ssh config host 등록 및, ec2 접속해서 루트계정 작업 해놓을 것
- ec2에 jdk 1.8.0 설치
- kafaka 3.1.0 with scala 2.12.0 설치

### SASL_PLAINTEXT 인증설정

/config/server.properties 설정 추가 및 변경

```bash
listeners=SASL_PLAINTEXT://${ec2-private-ip}:9092
security.inter.broker.protocol=SASL_PLAINTEXT
advertised.listeners=SASL_PLAINTEXT://${ec2-public-ip}:9092
sasl.enabled.mechanisms=PLAIN
sasl.mechanism.inter.broker.protocol=PLAIN
authorizer.class.name=kafka.security.authorizer.AclAuthorizer
allow.everyone.if.no.acl.found=true
```

/config/zookeeper.properties 설정 추가 및 변경

```bash
authProvider.1=org.apache.zookeeper.server.auth.SASLAuthenticationProvider
requireClientAuthScheme=sasl
jaasLoginRenew=3600000
```

kafka_server_jaas.conf

```bash
KafkaServer { # 클라이언트 -> 카프카 서버 인증에 사용
        org.apache.kafka.common.security.plain.PlainLoginModule required
        username="admin"
        password="admin-secret"
        user_admin="admin-secret"
        user_reason="admin-reason";
};

Client { # 카프카 서버 -> 주키퍼 인증에 사용
    org.apache.zookeeper.server.auth.DigestLoginModule required
    username="admin"
    password="admin-secret";
};
```

zookeeper_jaas.conf

```bash
# 카프카 서버 -> 주키퍼 인증에 사용
Server {
        org.apache.zookeeper.server.auth.DigestLoginModule required
        user_super="zookeeper"
        user_admin="admin-secret";
};
```

console.properties / 머신 내에서 cli를 이용한 테스트를 위해 사용

```bash
security.protocol=SASL_PLAINTEXT
sasl.mechanism=PLAIN
sasl.jaas.config=org.apache.kafka.common.security.plain.PlainLoginModule required \
    username="reason" \
    password="admin-reason";
```

실행 및 테스트

```bash
# zookeeper 실행
export KAFKA_OPTS="-Djava.security.auth.login.config=/home/ec2-user/kafka_2.12-3.1.0/config/jaas/zookeeper_server_jaas.conf"
bin/zookeeper-server-start.sh config/zookeeper.properties

# kafka 실행
export KAFKA_OPTS="-Djava.security.auth.login.config=/home/ec2-user/kafka_2.12-3.1.0/config/jaas/kafka_server_jaas.conf"
export KAFKA_HEAP_OPTS="-Xms400m -Xmx400m"
bin/kafka-server-start.sh config/server.properties

# topic test
/home/ec2-user/kafka_2.12-3.1.0/bin/kafka-topics.sh --bootstrap-server ${ec2-private-ip}:9092 --list --command-config /home/ec2-user/kafka_2.12-3.1.0/config/console/config.properties
```