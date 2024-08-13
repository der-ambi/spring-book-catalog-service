package de.bashburg.springbook.catalogservice.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("polar")
data class PolarProperties(
    /**
     * A message to welcome users.
     */
    val greeting: String
)
