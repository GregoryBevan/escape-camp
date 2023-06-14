package me.elgregos.escapecamp.game.domain.event

import me.elgregos.escapecamp.config.security.organizerId
import me.elgregos.escapecamp.game.domain.entity.*
import me.elgregos.escapecamp.game.domain.event.GameEvent.GameCreated
import me.elgregos.escapecamp.game.domain.event.GameEvent.TeamAdded
import me.elgregos.reakteves.libs.genericObjectMapper


val escapeCampCreated = GameCreated(
    gameId = escapeCampId,
    createdAt = escapeCampCreatedAt,
    createdBy = organizerId,
    event = genericObjectMapper.createObjectNode()
        .put("id", "$escapeCampId")
        .put("createdAt", "$escapeCampCreatedAt")
        .put("createdBy", "$organizerId")
        .set("riddles", genericObjectMapper.valueToTree(riddles))
)

val lockedAndLoadedTeamAdded = TeamAdded(
    gameId = escapeCampId,
    version = 2,
    addedAt = lockedAndLoadedTeamAddedAt,
    addedBy = lockedAndLoadedTeamId,
    event = genericObjectMapper.createObjectNode()
        .put("id", "981e1b04-ecc6-48b3-b750-58f20faa5e05")
        .put("updatedBy", "0bfce65c-dff9-4f2e-8e8f-11ed6151b205")
        .put("updatedAt", "2023-06-15T13:31")
        .set(
            "teams",
            genericObjectMapper.createArrayNode()
                .add(lockedAndLoadedTeam.toJson())
        )
)

val eventsAfterLockedAndLoadedAdded = listOf(escapeCampCreated, lockedAndLoadedTeamAdded)

val jeepersKeypersTeamAdded = TeamAdded(
    gameId = escapeCampId,
    version = 3,
    addedAt = jeepersKeypersTeamAddedAt,
    addedBy = jeepersKeypersTeamId,
    event = genericObjectMapper.createObjectNode()
        .put("id", "981e1b04-ecc6-48b3-b750-58f20faa5e05")
        .put("updatedBy", "3a66dce7-ca96-4cd0-af56-6bd7e082edd5")
        .put("updatedAt", "2023-06-15T13:35")
        .set(
            "teams",
            genericObjectMapper.createArrayNode()
                .add(lockedAndLoadedTeam.toJson())
                .add(jeepersKeypersTeam.toJson())
        ),
)

val escapeCampStarted =
    GameEvent.GameStarted(
        gameId = escapeCampId,
        version = 4,
        startedAt = jeepersKeypersTeamAddedAt,
        startedBy = jeepersKeypersTeamId
    )

val eventsAfterEscapeCampStarted =
    listOf(escapeCampCreated, lockedAndLoadedTeamAdded, jeepersKeypersTeamAdded, escapeCampStarted)

val lockedAndLoadedFirstRiddleAssigned =
    GameEvent.NextTeamRiddleAssigned(
        gameId = escapeCampId,
        version = 5,
        assignedAt = lockedAndLoadedFirstRiddleAssignedAt,
        assignedBy = lockedAndLoadedTeamId,
        event = genericObjectMapper.createObjectNode()
            .put("id", "981e1b04-ecc6-48b3-b750-58f20faa5e05")
            .put("updatedAt", "2023-06-15T13:36:12")
            .put("updatedBy", "0bfce65c-dff9-4f2e-8e8f-11ed6151b205")
            .set(
                "teams",
                genericObjectMapper.createArrayNode()
                    .add(lockedAndLoadedTeamAfterFirstRiddleAssigned.toJson())
                    .add(jeepersKeypersTeam.toJson())
            )
    )

val eventsAfterLockedAndLoadedFirstRiddleAssigned = listOf(
    escapeCampCreated,
    lockedAndLoadedTeamAdded,
    jeepersKeypersTeamAdded,
    escapeCampStarted,
    lockedAndLoadedFirstRiddleAssigned
)

val jeepersKeypersFirstRiddleAssigned =
    GameEvent.NextTeamRiddleAssigned(
        gameId = escapeCampId,
        version = 6,
        assignedAt = jeepersKeypersFirstRiddleAssignedAt,
        assignedBy = jeepersKeypersTeamId,
        event = genericObjectMapper.createObjectNode()
            .put("id", "981e1b04-ecc6-48b3-b750-58f20faa5e05")
            .put("updatedAt", "2023-06-15T13:37:04")
            .put("updatedBy", "3a66dce7-ca96-4cd0-af56-6bd7e082edd5")
            .set(
                "teams",
                genericObjectMapper.createArrayNode()
                    .add(lockedAndLoadedTeamAfterFirstRiddleAssigned.toJson())
                    .add(jeepersKeypersTeamAfterFirstRiddleAssigned.toJson())
            )
    )

val eventsAfterAllFirstRiddleAssigned = listOf(
    escapeCampCreated,
    lockedAndLoadedTeamAdded,
    jeepersKeypersTeamAdded,
    escapeCampStarted,
    lockedAndLoadedFirstRiddleAssigned,
    jeepersKeypersFirstRiddleAssigned
)

val jeepersKeypersFirstRiddleSolved =
    GameEvent.RiddleSolved(
        gameId = escapeCampId,
        version = 7,
        solvedAt = jeepersKeypersFirstRiddleSolvedAt,
        solvedBy = jeepersKeypersTeamId,
        event = genericObjectMapper.createObjectNode()
            .put("id", "981e1b04-ecc6-48b3-b750-58f20faa5e05")
            .put("updatedAt", "2023-06-15T13:41:45")
            .put("updatedBy", "3a66dce7-ca96-4cd0-af56-6bd7e082edd5")
            .set(
                "teams",
                genericObjectMapper.createArrayNode()
                    .add(lockedAndLoadedTeamAfterFirstRiddleAssigned.toJson())
                    .add(jeepersKeypersTeamAfterFirstRiddleSolved.toJson())
            )
    )

val eventsAfterJeepersKeypersFirstRiddleSolved = listOf(
    escapeCampCreated,
    lockedAndLoadedTeamAdded,
    jeepersKeypersTeamAdded,
    escapeCampStarted,
    lockedAndLoadedFirstRiddleAssigned,
    jeepersKeypersFirstRiddleAssigned,
    jeepersKeypersFirstRiddleSolved
)

val jeepersKeypersSecondRiddleAssigned =
    GameEvent.NextTeamRiddleAssigned(
        gameId = escapeCampId,
        version = 8,
        assignedAt = jeepersKeypersSecondRiddleAssignedAt,
        assignedBy = jeepersKeypersTeamId,
        event = genericObjectMapper.createObjectNode()
            .put("id", "981e1b04-ecc6-48b3-b750-58f20faa5e05")
            .put("updatedAt", "2023-06-15T13:42:28")
            .put("updatedBy", "3a66dce7-ca96-4cd0-af56-6bd7e082edd5")
            .set(
                "teams",
                genericObjectMapper.createArrayNode()
                    .add(lockedAndLoadedTeamAfterFirstRiddleAssigned.toJson())
                    .add(jeepersKeypersTeamAfterSecondRiddleAssigned.toJson())
            )
    )

val lockedAndLoadedSecondRiddleAssigned =
    GameEvent.NextTeamRiddleAssigned(
        gameId = escapeCampId,
        version = 9,
        assignedAt = lockedAndLoadedSecondRiddleAssignedAt,
        assignedBy = lockedAndLoadedTeamId,
        event = genericObjectMapper.createObjectNode()
            .put("id", "981e1b04-ecc6-48b3-b750-58f20faa5e05")
            .put("updatedAt", "2023-06-15T13:44:02")
            .put("updatedBy", "0bfce65c-dff9-4f2e-8e8f-11ed6151b205")
            .set(
                "teams",
                genericObjectMapper.createArrayNode()
                    .add(lockedAndLoadedTeamAfterSecondRiddleAssigned.toJson())
                    .add(jeepersKeypersTeamAfterSecondRiddleAssigned.toJson())
            )
    )

val eventsAfterLockedAndLoadedSecondRiddleAssigned = listOf(
    escapeCampCreated,
    lockedAndLoadedTeamAdded,
    jeepersKeypersTeamAdded,
    escapeCampStarted,
    lockedAndLoadedFirstRiddleAssigned,
    jeepersKeypersFirstRiddleAssigned,
    jeepersKeypersFirstRiddleSolved,
    jeepersKeypersSecondRiddleAssigned,
    lockedAndLoadedSecondRiddleAssigned
)

val jeepersKeypersSecondRiddleSolved =
    GameEvent.RiddleSolved(
        gameId = escapeCampId,
        version = 10,
        solvedAt = jeepersKeypersSecondRiddleSolvedAt,
        solvedBy = jeepersKeypersTeamId,
        event = genericObjectMapper.createObjectNode()
            .put("id", "981e1b04-ecc6-48b3-b750-58f20faa5e05")
            .put("updatedAt", "2023-06-15T13:47:36")
            .put("updatedBy", "3a66dce7-ca96-4cd0-af56-6bd7e082edd5")
            .set(
                "teams",
                genericObjectMapper.createArrayNode()
                    .add(lockedAndLoadedTeamAfterSecondRiddleAssigned.toJson())
                    .add(jeepersKeypersTeamAfterSecondRiddleSolved.toJson())
            )
    )

val escapeCampWinnerDefined =
    GameEvent.WinnerDefined(
        gameId = escapeCampId,
        version = 11,
        definedAt = jeepersKeypersSecondRiddleSolvedAt,
        definedBy = jeepersKeypersTeamId,
        event = genericObjectMapper.createObjectNode()
            .put("id", "981e1b04-ecc6-48b3-b750-58f20faa5e05")
            .put("winner", "3a66dce7-ca96-4cd0-af56-6bd7e082edd5"))



val eventsAfterWinnerDefined = listOf(
    escapeCampCreated,
    lockedAndLoadedTeamAdded,
    jeepersKeypersTeamAdded,
    escapeCampStarted,
    lockedAndLoadedFirstRiddleAssigned,
    jeepersKeypersFirstRiddleAssigned,
    jeepersKeypersFirstRiddleSolved,
    jeepersKeypersSecondRiddleAssigned,
    lockedAndLoadedSecondRiddleAssigned,
    jeepersKeypersSecondRiddleSolved,
    escapeCampWinnerDefined
)

val lockedAndLoadedSecondRiddleSolved =
    GameEvent.RiddleSolved(
        gameId = escapeCampId,
        version = 12,
        solvedAt = lockedAndLoadedSecondRiddleSolvedAt,
        solvedBy = lockedAndLoadedTeamId,
        event = genericObjectMapper.createObjectNode()
            .put("id", "981e1b04-ecc6-48b3-b750-58f20faa5e05")
            .put("updatedAt", "2023-06-15T13:50:05")
            .put("updatedBy", "0bfce65c-dff9-4f2e-8e8f-11ed6151b205")
            .set(
                "teams",
                genericObjectMapper.createArrayNode()
                    .add(lockedAndLoadedTeamAfterSecondRiddleSolved.toJson())
                    .add(jeepersKeypersTeamAfterSecondRiddleSolved.toJson())
            )
    )