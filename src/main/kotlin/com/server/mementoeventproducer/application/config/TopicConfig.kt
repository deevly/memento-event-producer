package com.server.mementoeventproducer.application.config

import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.producer.ProducerConfig
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
    fun githubTopic(): NewTopic? {
        return TopicBuilder.name("github")
            .partitions(1)
            .replicas(1)
            .compact()
            .build()
    }

    @Bean
    fun stackOverFlowTopic(): NewTopic? {
        return TopicBuilder.name("stackOverFlow")
            .partitions(10)
            .replicas(1)
            .compact()
            .build()
    }
}