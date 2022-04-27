package com.server.mementoeventproducer.application.port.`in`

interface HistoryEventUseCase {

    fun produceHistoryEvent(request: CreateHistoryEventRequest)
}