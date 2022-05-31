package com.memento.eventproducer.application.service

import com.memento.eventproducer.application.common.SiteDomain
import com.memento.eventproducer.application.port.`in`.CreateHistoryEventRequest
import com.memento.eventproducer.application.port.`in`.HistoryEventUseCase
import com.memento.eventproducer.application.port.out.HistoryEvent
import com.memento.eventproducer.application.port.out.SendMessagePort
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

        return HistoryEvent(request.user, request.keyword, request.url, request.visitedTime.toString(),
            SiteDomain.fromPrefixUrl(getPrefixUrl(request.url)))
    }

    private fun getPrefixUrl(url : String): String {
        val prefixLastIndex = url.indexOf("/", 8);
        return if (prefixLastIndex == -1) {
            url
        } else {
            url.substring(0, prefixLastIndex)
        }
    }

}