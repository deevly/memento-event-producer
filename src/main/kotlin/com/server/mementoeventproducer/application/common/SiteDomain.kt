package com.server.mementoeventproducer.application.common

enum class SiteDomain(
    val fullName : String,
    val prefixUrl : String
) {

    GITHUB("Github", "www.github.com"),
    STACKOF("Stack Overflow", "www.stackoverflow.com")
}
