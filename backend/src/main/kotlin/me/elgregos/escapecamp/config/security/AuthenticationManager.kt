package me.elgregos.escapecamp.config.security

import me.elgregos.escapecamp.config.security.jwt.TokenProvider
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class AuthenticationManager(private val tokenProvider: TokenProvider): ReactiveAuthenticationManager {

    override fun authenticate( authentication: Authentication): Mono<Authentication> =
            Mono.just(authentication.credentials as String)
            .filter { tokenProvider.validateToken(it) }
            .map { tokenProvider.authenticatedUser(it) }
            .map { UsernamePasswordAuthenticationToken(it, null, it.authorities) }
}
