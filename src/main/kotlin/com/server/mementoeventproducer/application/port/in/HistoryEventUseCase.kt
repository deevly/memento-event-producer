package com.server.mementoeventproducer.application.port.`in`

import com.server.mementoeventproducer.application.dto.CreateHistoryEventRequest

interface HistoryEventUseCase {

    fun produceHistoryEvent(request: CreateHistoryEventRequest)
}