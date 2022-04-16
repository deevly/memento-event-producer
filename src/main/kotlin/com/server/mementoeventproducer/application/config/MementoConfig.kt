package com.server.mementoeventproducer.application.config

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "memento")
data class MementoConfig (
    val kafka: KafkaProperties,
) {
    data class KafkaProperties(
        val bootstrapAddresses: String,
        val username: String,
        val password: String
    )
}
