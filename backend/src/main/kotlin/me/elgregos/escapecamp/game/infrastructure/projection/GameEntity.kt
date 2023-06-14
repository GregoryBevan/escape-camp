package me.elgregos.escapecamp.game.infrastructure.projection

import com.fasterxml.jackson.databind.JsonNode
import me.elgregos.escapecamp.game.domain.entity.Game
import me.elgregos.reakteves.domain.JsonConvertible
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.*

@Table("game")
data class GameEntity(
    @Id private val id: UUID,
    val sequenceNum: Long? = null,
    val version: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime ,
    private val details: JsonNode
) : Persistable<UUID> {

    @Transient
    private var isNew = false

    override fun getId() = id

    @Transient
    override fun isNew() = isNew

    fun markNew() {
        isNew = true
    }

    fun toGame() =
        JsonConvertible.fromJson(details, Game::class.java)

    companion object {
        fun fromGame(game: Game) =
            GameEntity(
                id = game.id,
                version = game.version,
                createdAt = game.createdAt,
                updatedAt = game.updatedAt,
                details = game.toJson()
            )
    }
}
