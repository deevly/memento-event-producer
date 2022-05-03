package com.memento.eventproducer.application.port.out

import com.memento.eventproducer.application.common.SiteDomain
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime

data class HistoryEvent(
    val user: String,
    val keyword: String,
    val url: String,
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val visitedTime: LocalDateTime,
    val siteDomain: SiteDomain
)
