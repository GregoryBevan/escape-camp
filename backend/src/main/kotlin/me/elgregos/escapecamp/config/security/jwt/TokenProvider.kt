package me.elgregos.escapecamp.config.security.jwt

import io.jsonwebtoken.*
import io.jsonwebtoken.jackson.io.JacksonSerializer
import io.jsonwebtoken.security.Keys
import jakarta.annotation.PostConstruct
import me.elgregos.escapecamp.config.security.AuthenticatedUser
import me.elgregos.escapecamp.config.security.Role
import me.elgregos.reakteves.libs.genericObjectMapper
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.SecretKey


@Service
class TokenProvider(private val jwtConfig: JwtConfig) {

    private lateinit var signingKey: SecretKey

    @PostConstruct
    protected fun init() {
        signingKey = Keys.hmacShaKeyFor(jwtConfig.secretKey.encodeToByteArray())
    }

    fun generateAccessToken(subject: UUID, name: String, role: Role): String {
        val issuedAt = Date()
        val expireAt = Date(issuedAt.time + jwtConfig.accessTokenExpiration.toMillis())
        return Jwts.builder()
            .json(JacksonSerializer(genericObjectMapper))
            .subject(subject.toString())
            .issuer(jwtConfig.issuer)
            .issuedAt(issuedAt)
            .expiration(expireAt)
            .claims().add(otherUserClaims(name, role))
            .and()
            .signWith(signingKey)
            .compact()
    }

    fun validateToken(token: String) = try {
        val claims: Jws<Claims> = parser().parseSignedClaims(token)
        !claims.payload.expiration.before(Date())
    } catch (e: JwtException) {
        false
    } catch (e: IllegalArgumentException) {
        false
    }

    fun authenticatedUser(token: String): AuthenticatedUser {
        val claims: Claims = parser().parseSignedClaims(token).payload
        return AuthenticatedUser(
            id = UUID.fromString(claims.subject),
            username = claims["username", String::class.java],
            role = Role.valueOf(claims["role", String::class.java])
        )
    }

    private fun otherUserClaims(name: String, role: Role) = mapOf(Pair("username", name), Pair("role", role.name))


    private fun parser(): JwtParser {
        return Jwts.parser().verifyWith(signingKey).build()
    }
}
