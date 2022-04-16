package com.server.mementoeventproducer.application.config

import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.common.config.SaslConfigs
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.KafkaAdmin


@Configuration
class TopicConfig (
    private val mementoConfig: MementoConfig)
{

    @Bean
    fun admin(): KafkaAdmin? {
        val configProps: MutableMap<String, Any> = HashMap()
        configProps[AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG] = mementoConfig.kafka.bootstrapAddresses
        configProps[SaslConfigs.SASL_MECHANISM] = "PLAIN"
        configProps[SaslConfigs.SASL_JAAS_CONFIG] = "org.apache.kafka.common.security.plain.PlainLoginModule   required username='reason'   password='admin-reason';"
        configProps[CommonClientConfigs.SECURITY_PROTOCOL_CONFIG] = "SASL_PLAINTEXT"
        return KafkaAdmin(configProps)
    }

    @Bean
    fun topic1(): NewTopic? {
        return TopicBuilder.name("topic")
            .partitions(10)
            .replicas(1)
            .compact()
            .build()
    }
}