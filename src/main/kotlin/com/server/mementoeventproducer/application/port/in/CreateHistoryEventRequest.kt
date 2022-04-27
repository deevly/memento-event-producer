package com.server.mementoeventproducer.application.port.`in`

import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime

data class CreateHistoryEventRequest(
    val user: String,
    val keyword: String,
    val url: String,
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val visitedTime: LocalDateTime
)
