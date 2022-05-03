package com.memento.eventproducer.adaptor.`in`

import com.memento.eventproducer.application.port.`in`.CreateHistoryEventRequest
import com.memento.eventproducer.application.port.`in`.HistoryEventUseCase
import mu.KLogging
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@RestController
class HistoryEventController(
    private val historyEventUseCase: HistoryEventUseCase
) {
    private val logger = KLogging().logger

    @PostMapping("/event")
    fun createHistoryEvent(@RequestBody request: CreateHistoryEventRequest) {
        logger.info{ request.toString() }
        historyEventUseCase.produceHistoryEvent(request)
    }
}