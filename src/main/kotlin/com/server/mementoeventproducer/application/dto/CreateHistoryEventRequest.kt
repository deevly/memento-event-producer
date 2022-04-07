package com.server.mementoeventproducer.application.dto

data class CreateHistoryEventRequest(
    val type: String,
    val url: String
)
