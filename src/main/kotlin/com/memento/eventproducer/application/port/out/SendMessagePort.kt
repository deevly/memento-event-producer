package com.memento.eventproducer.application.port.out

import org.springframework.messaging.Message

interface SendMessagePort {

    fun sendHistoryEventMessage(topic: String, message: Message<HistoryEvent>)
}