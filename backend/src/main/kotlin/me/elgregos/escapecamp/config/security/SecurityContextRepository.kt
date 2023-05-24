package me.elgregos.escapecamp.config.security

import org.springframework.http.HttpHeaders
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class SecurityContextRepository(val authenticationManager: AuthenticationManager) :
    ServerSecurityContextRepository {

    override fun save(exchange: ServerWebExchange, context: SecurityContext): Mono<Void> =
        throw UnsupportedOperationException("Not supported yet.")

    override fun load(exchange: ServerWebExchange): Mono<SecurityContext> =
        Mono.justOrEmpty<String>(exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION))
            .filter { it.startsWith("Bearer ") }
            .map { it.substring(7) }
            .map { PreAuthenticatedAuthenticationToken(it, it)}
            .flatMap { this.authenticationManager.authenticate(it).map { authentication -> SecurityContextImpl(authentication) } }
}