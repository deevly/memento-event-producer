package com.server.mementoeventproducer.application.service

import com.server.mementoeventproducer.application.common.SiteDomain
import com.server.mementoeventproducer.application.port.`in`.CreateHistoryEventRequest
import com.server.mementoeventproducer.application.port.`in`.HistoryEventUseCase
import com.server.mementoeventproducer.application.port.out.HistoryEvent
import com.server.mementoeventproducer.application.port.out.SendMessagePort
import lombok.RequiredArgsConstructor
import mu.KLogging
import org.springframework.messaging.Message
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class HistoryEventService (
    private val sendMessagePort: SendMessagePort
        ): HistoryEventUseCase {

    private val logger = KLogging().logger

    override fun produceHistoryEvent(request: CreateHistoryEventRequest) {

        val historyEvent: HistoryEvent = getHistoryEvent(request)

        val message: Message<HistoryEvent> = MessageBuilder
            .withPayload(historyEvent)
            .build();

        sendMessagePort.sendHistoryEventMessage(historyEvent.siteDomain.topic, message)
    }

    private fun getHistoryEvent(request: CreateHistoryEventRequest): HistoryEvent {

        val siteDomain: SiteDomain = SiteDomain.fromPrefixUrl(request.url)
        return HistoryEvent(request.user, request.keyword, request.url, request.visitedTime, siteDomain)
    }

}