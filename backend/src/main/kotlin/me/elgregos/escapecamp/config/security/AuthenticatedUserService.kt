package me.elgregos.escapecamp.config.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class AuthenticatedUserService(
    @Value("\${organizer.password}") password: String,
    passwordEncoder: PasswordEncoder,
    private val users: Map<String, UserDetails> = mapOf(
        Pair(
            organizer.username,
            organizer.copy(password = passwordEncoder.encode(password))
        )
    )
) : ReactiveUserDetailsService {


    override fun findByUsername(username: String) =
        Mono.just(this.users[username] ?: throw UsernameNotFoundException("$username not found"))
}