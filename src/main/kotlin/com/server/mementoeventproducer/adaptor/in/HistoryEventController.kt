package com.server.mementoeventproducer.adaptor.`in`

import com.server.mementoeventproducer.application.common.SiteDomain
import com.server.mementoeventproducer.application.dto.CreateHistoryEventRequest
import com.server.mementoeventproducer.application.port.`in`.HistoryEventUseCase
import mu.KLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController



@RestController
class HistoryEventController(
    private val historyEventUseCase: HistoryEventUseCase
) {

    private val logger = KLogging().logger

    @GetMapping("/event")
    fun createHistoryEvent() {
        logger.info("hello")
        historyEventUseCase.produceHistoryEvent(CreateHistoryEventRequest(SiteDomain.GITHUB, "URL"))
    }
}