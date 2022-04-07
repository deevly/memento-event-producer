package com.server.mementoeventproducer.adaptor.`in`

import mu.KLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

private val logger = KLogging().logger

@RestController
class HistoryEventController {

    @GetMapping("/event")
    fun createHistoryEvent() {
        logger.info("hello")
    }
}