package com.server.mementoeventproducer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan
@SpringBootApplication
class MementoEventProducerApplication

fun main(args: Array<String>) {
    runApplication<MementoEventProducerApplication>(*args)
}
