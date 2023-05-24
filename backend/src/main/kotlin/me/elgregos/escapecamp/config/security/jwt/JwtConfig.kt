package me.elgregos.escapecamp.config.security.jwt

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding
import java.time.Duration


@ConfigurationProperties(prefix = "jwt")
data class JwtConfig @ConstructorBinding constructor(
    val secretKey: String,
    val accessTokenExpiration: Duration,
    val issuer: String
)