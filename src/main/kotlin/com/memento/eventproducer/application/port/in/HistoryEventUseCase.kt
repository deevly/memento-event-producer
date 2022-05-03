package com.memento.eventproducer.application.port.`in`

interface HistoryEventUseCase {

    fun produceHistoryEvent(request: CreateHistoryEventRequest)
}