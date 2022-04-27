package com.server.mementoeventproducer.application.common

enum class SiteDomain(
    val topic: String,
    val prefixUrl: String
) {

    GITHUB("github", "www.github.com"),
    STACKOF("stackOverFlow", "www.stackoverflow.com"),
    OTHERS("Others", "etc");

    companion object {

        private val map = values().associateBy(SiteDomain::prefixUrl)
        fun fromPrefixUrl(prefixUrl: String) = map[prefixUrl] ?: OTHERS
    }
}
