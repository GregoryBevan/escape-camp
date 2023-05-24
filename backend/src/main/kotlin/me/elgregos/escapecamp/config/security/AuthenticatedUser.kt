package me.elgregos.escapecamp.config.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import java.util.*

data class AuthenticatedUser(
    val id: UUID,
    @get:JvmName("username")
    val username: String,
    @get:JvmName("password")
    val password: String = "",
    val role: Role,
    @get:JvmName("authorities")
    val authorities: Collection<GrantedAuthority> = listOf(SimpleGrantedAuthority(role.name))
): User(username, password, authorities)

val organizer = AuthenticatedUser(id = UUID.fromString("2d399fb8-d137-47a5-b848-1755bb80e62e"), username = "organizer", role = Role.ORGANIZER)

enum class Role {
    ORGANIZER,
    PLAYER
}