package me.elgregos.escapecamp.game.application

import me.elgregos.escapecamp.game.domain.entity.Contestant
import me.elgregos.escapecamp.game.domain.entity.EnrollmentType
import me.elgregos.reakteves.application.Command
import me.elgregos.reakteves.libs.nowUTC
import java.time.LocalDateTime
import java.util.*

sealed class GameCommand(open val gameId: UUID) : Command {

    data class CreateGame(
        override val gameId: UUID = UUID.randomUUID(),
        val createdBy: UUID,
        val createdAt: LocalDateTime = nowUTC(),
        val riddles: List<Pair<String, String>>,
        val enrollmentType: EnrollmentType
    ) : GameCommand(gameId)

    data class EnrollContestant(
        override val gameId: UUID,
        val enrolledAt: LocalDateTime = nowUTC(),
        val enrolledBy: UUID = UUID.randomUUID(),
        val name: String,
        val contestant: Contestant = Contestant(enrolledBy, name)
    ) : GameCommand(gameId)

    data class UnlockNextRiddle(
        override val gameId: UUID,
        val unlockedAt: LocalDateTime = nowUTC(),
        val unlockedBy: UUID
    ) : GameCommand(gameId)

    data class AssignContestantNextRiddle(
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
