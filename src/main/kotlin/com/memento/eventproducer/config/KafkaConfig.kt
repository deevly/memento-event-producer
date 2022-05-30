package com.memento.eventproducer.config

import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.config.SaslConfigs
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaAdmin
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.serializer.JsonSerializer

@Configuration
class KafkaConfig (
    private val mementoConfig: MementoConfig
        ) {

    val decryptedUsername = AwsDescryptor.getPlainText(mementoConfig.kafka.username, mementoConfig.kms.keyId)
    val decryptedPassword = AwsDescryptor.getPlainText(mementoConfig.kafka.password, mementoConfig.kms.keyId)
    val jaasConfig = String.format("org.apache.kafka.common.security.plain.PlainLoginModule   " +
            "required username='$decryptedUsername'   password='$decryptedPassword';")

    @Bean
    fun producerFactory(): ProducerFactory<String, Any> {
        val configProps = producerFactoryConfig()
        return DefaultKafkaProducerFactory(configProps)
    }

    private fun producerFactoryConfig(): Map<String, Any> {
        val configProps: MutableMap<String, Any> = HashMap()
        configProps[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = mementoConfig.kafka.bootstrapAddresses
        configProps[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        configProps[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = JsonSerializer::class.java
        configProps[ProducerConfig.BATCH_SIZE_CONFIG] = 10
        configProps[SaslConfigs.SASL_MECHANISM] = "PLAIN"
        configProps[SaslConfigs.SASL_JAAS_CONFIG] = jaasConfig
        configProps[CommonClientConfigs.SECURITY_PROTOCOL_CONFIG] = "SASL_PLAINTEXT"
        return configProps
    }

    @Bean
    fun kafkaTemplate(): KafkaTemplate<String, Any> {
        return KafkaTemplate(producerFactory())
    }

    @Bean
    fun admin(): KafkaAdmin? {
        val configProps: MutableMap<String, Any> = HashMap()
        configProps[AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG] = mementoConfig.kafka.bootstrapAddresses
        configProps[SaslConfigs.SASL_MECHANISM] = "PLAIN"
        configProps[SaslConfigs.SASL_JAAS_CONFIG] = jaasConfig
        configProps[CommonClientConfigs.SECURITY_PROTOCOL_CONFIG] = "SASL_PLAINTEXT"
        return KafkaAdmin(configProps)
    }

    @Bean
    fun githubTopic(): NewTopic? {
        return TopicBuilder.name("github")
            .partitions(1)
            .replicas(1)
            .compact()
            .build()
    }

    @Bean
    fun stackOverflowTopic(): NewTopic? {
        return TopicBuilder.name("stackOverflow")
            .partitions(1)
            .replicas(1)
            .compact()
            .build()
    }

    @Bean
    fun anotherTopic(): NewTopic? {
        return TopicBuilder.name("others")
            .partitions(1)
            .replicas(1)
            .compact()
            .build()
    }
}