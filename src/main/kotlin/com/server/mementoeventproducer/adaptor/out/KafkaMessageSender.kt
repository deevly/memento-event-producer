package com.server.mementoeventproducer.adaptor.out

import com.server.mementoeventproducer.application.port.out.HistoryEvent
import com.server.mementoeventproducer.application.port.out.SendMessagePort
import mu.KLogging
import org.springframework.kafka.core.KafkaProducerException
import org.springframework.kafka.core.KafkaSendCallback
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.messaging.Message
import org.springframework.stereotype.Service

@Service
class KafkaMessageSender (
    private val kafkaTemplate: KafkaTemplate<String, Any>
        ) : SendMessagePort {

    private val logger = KLogging().logger

    override fun sendHistoryEventMessage(topic: String, message: Message<HistoryEvent>) {
        val future = kafkaTemplate.send(topic, message);

        future.addCallback(listenableFutureCallback(message))
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
                    "Failure [ $message ] due to: ${ex.getFailedProducerRecord<String, String>()}"
                )
            }
        }
}