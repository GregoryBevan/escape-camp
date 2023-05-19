package me.elgregos.escapecamp.game.domain.entity

import me.elgregos.reakteves.domain.JsonConvertible
import java.util.*

data class Team(val id: UUID, val name: String): JsonConvertible