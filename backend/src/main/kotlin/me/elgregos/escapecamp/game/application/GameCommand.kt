package me.elgregos.escapecamp.game.application

import me.elgregos.escapecamp.game.domain.entity.Team
import me.elgregos.reakteves.application.Command
import me.elgregos.reakteves.libs.nowUTC
import java.time.LocalDateTime
import java.util.*

sealed class GameCommand(open val gameId: UUID) : Command {

    data class CreateGame(
        override val gameId: UUID = UUID.randomUUID(),
        val createdBy: UUID,
        val createdAt: LocalDateTime = nowUTC()
    ) : GameCommand(gameId)

    data class AddTeam(
        override val gameId: UUID,
        val addedAt: LocalDateTime = nowUTC(),
        val addedBy: UUID = UUID.randomUUID(),
        val name: String,
        val team: Team = Team(addedBy, name)
    ) : GameCommand(gameId)

    data class AssignTeamNextRiddle(
        override val gameId: UUID,
        val assignedAt: LocalDateTime = nowUTC(),
        val assignedBy: UUID,
    ) : GameCommand(gameId)

    data class SubmitRiddleSolution(
        override val gameId: UUID,
        val submittedAt: LocalDateTime = nowUTC(),
        val submittedBy: UUID,
        val riddleName: String,
        val solution: String
    ) : GameCommand(gameId)
}