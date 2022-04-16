package com.server.mementoeventproducer.application.dto

import com.server.mementoeventproducer.application.common.SiteDomain

data class CreateHistoryEventRequest(
    val type: SiteDomain,
    val url: String
)
