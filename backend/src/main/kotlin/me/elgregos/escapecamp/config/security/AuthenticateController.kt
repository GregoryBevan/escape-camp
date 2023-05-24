package me.elgregos.escapecamp.config.security

import jakarta.validation.Valid
import me.elgregos.escapecamp.config.security.jwt.TokenProvider
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping(path = ["api/tokens"])
class AuthenticateController(
    val tokenProvider: TokenProvider,
    val userDetailsService: ReactiveUserDetailsService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    fun authenticate(@Valid @RequestBody credential: Credential) =
        userDetailsService.findByUsername(credential.username)
            .map { it as AuthenticatedUser }
            .map { tokenProvider.generateAccessToken(it.id, it.username, it.role) }
            .map { jwt ->
                val httpHeaders = HttpHeaders()
                httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer $jwt")
                val tokenBody = mapOf(Pair("accessToken", jwt))
                ResponseEntity<Any?>(tokenBody, httpHeaders, HttpStatus.OK)
            }

    data class Credential(val username: String, val password: String)
}