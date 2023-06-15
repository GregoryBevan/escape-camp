package me.elgregos.escapecamp.config.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain


@Configuration
@EnableWebFluxSecurity
class SecurityConfig(
    val authenticationManager: AuthenticationManager,
    val securityContextRepository: SecurityContextRepository
) {

    @Bean
    fun passwordEncoder(): PasswordEncoder? {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }

    @Bean
    fun springSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain =
        http
            .csrf().disable()
            .formLogin().disable()
            .httpBasic().disable()
            .authenticationManager(authenticationManager)
            .securityContextRepository(securityContextRepository)
            .authorizeExchange()
            .pathMatchers(HttpMethod.GET, "/index.html", "/assets/**","/sw.js", "/favicon.ico").permitAll()
            .pathMatchers(HttpMethod.POST, "/api/tokens").permitAll()
            .pathMatchers(
                HttpMethod.POST,
                "/api/games/{id:[a-fA-F0-9]{8}\\-[a-fA-F0-9]{4}\\-4[a-fA-F0-9]{3}\\-[89abAB][a-fA-F0-9]{3}\\-[a-fA-F0-9]{12}}/teams"
            ).permitAll()
            .anyExchange().authenticated()
            .and()
            .build()
}
