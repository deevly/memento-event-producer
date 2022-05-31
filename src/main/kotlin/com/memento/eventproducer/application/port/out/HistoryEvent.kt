package com.memento.eventproducer.application.port.out

import com.memento.eventproducer.application.common.SiteDomain
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime

data class HistoryEvent(
    val user: String,
    val keyword: String,
    val url: String,
    val visitedTime: String,
    val siteDomain: SiteDomain
)
