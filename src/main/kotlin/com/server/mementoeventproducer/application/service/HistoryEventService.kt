package com.server.mementoeventproducer.application.service

import com.server.mementoeventproducer.application.dto.CreateHistoryEventRequest
import com.server.mementoeventproducer.application.port.`in`.HistoryEventUseCase
import lombok.RequiredArgsConstructor
import mu.KLogging
import org.springframework.kafka.core.KafkaProducerException
import org.springframework.kafka.core.KafkaSendCallback
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.messaging.Message
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class HistoryEventService (
    private val kafkaTemplate: KafkaTemplate<String, Any>
) : HistoryEventUseCase {

    private val logger = KLogging().logger

    override fun produceHistoryEvent(value : CreateHistoryEventRequest) {

        val message : Message<CreateHistoryEventRequest> = MessageBuilder
            .withPayload(value)
//            .setHeader(KafkaHeaders.TOPIC, "topic")
            .build();

        val future = kafkaTemplate.send(message);

        future.addCallback(listenableFutureCallback(message))

        kafkaTemplate.send("topic", value)
    }

    fun listenableFutureCallback(message: Any) =
        object: KafkaSendCallback<String, Any> {
            override fun onSuccess(result: SendResult<String, Any>?) {
                logger.info(
                    "Send Message = [ $message ] with offset=[ ${result!!.recordMetadata.offset()} ]"
                )
            }

            override fun onFailure(ex: KafkaProducerException) {
                logger.error(
                    "Message 전달 오류 [ $message ] due to: ${ex.getFailedProducerRecord<String, String>()}"
                )
            }
        }
}