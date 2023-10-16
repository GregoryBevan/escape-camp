package me.elgregos.escapecamp.config.security

import jakarta.validation.Valid
import me.elgregos.escapecamp.config.security.jwt.TokenProvider
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono


@RestController
@RequestMapping(path = ["api/tokens"])
class AuthenticateController(
    private val passwordEncoder: PasswordEncoder,
    private val tokenProvider: TokenProvider,
    private val userDetailsService: ReactiveUserDetailsService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    fun authenticate(@Valid @RequestBody credential: Credential) =
        userDetailsService.findByUsername(credential.username)
            .filter { passwordEncoder.matches(credential.password, it.password) }
            .switchIfEmpty(Mono.error(BadCredentialsException("Wrong username or password")))
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
