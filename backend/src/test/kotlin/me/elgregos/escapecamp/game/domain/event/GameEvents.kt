package me.elgregos.escapecamp.game.domain.event

import me.elgregos.escapecamp.config.security.organizerId
import me.elgregos.escapecamp.game.domain.entity.*
import me.elgregos.escapecamp.game.domain.event.GameEvent.GameCreated
import me.elgregos.escapecamp.game.domain.event.GameEvent.TeamAdded
import me.elgregos.reakteves.libs.genericObjectMapper
import java.time.LocalDateTime


val escapeCampCreated = GameCreated(
    gameId = escapeCampId,
    createdAt = escapeCampCreatedAt,
    createdBy = escapeCampCreatorId,
    event = genericObjectMapper.createObjectNode().put("id", "$escapeCampId").put("createdAt", "$escapeCampCreatedAt")
        .put("createdBy", "$organizerId")
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
        ),
)

val eventsAfterLockedAndLoadedAdded = listOf(escapeCampCreated, lockedAndLoadedTeamAdded)

val jeepersKeypersTeamAddedAt: LocalDateTime = LocalDateTime.of(2023, 6, 15, 13, 32)
val jeepersKeypersTeamAdded = TeamAdded(
    gameId = escapeCampId,
    version = 3,
    addedAt = jeepersKeypersTeamAddedAt,
    addedBy = jeepersKeypersTeamId,
    event = genericObjectMapper.createObjectNode()
        .put("id", "981e1b04-ecc6-48b3-b750-58f20faa5e05")
        .put("updatedBy", "3a66dce7-ca96-4cd0-af56-6bd7e082edd5")
        .put("updatedAt", "2023-06-15T13:32")
        .set(
            "teams",
            genericObjectMapper.createArrayNode()
                .add(lockedAndLoadedTeam.toJson())
                .add(jeepersKeypersTeam.toJson())
        ),
)
val theEscapePeasTeamAddedAt: LocalDateTime = LocalDateTime.of(2023, 6, 15, 13, 33)

val theEscapePeasTeamAdded = TeamAdded(
    gameId = escapeCampId,
    version = 4,
    addedAt = theEscapePeasTeamAddedAt,
    addedBy = theEscapePeasTeamId,
    event = genericObjectMapper.createObjectNode()
        .put("id", "981e1b04-ecc6-48b3-b750-58f20faa5e05")
        .put("updatedBy", "91700c93-10f9-474e-8176-811598a9aaef")
        .put("updatedAt", "2023-06-15T13:33")
        .set(
            "teams",
            genericObjectMapper.createArrayNode()
                .add(lockedAndLoadedTeam.toJson())
                .add(jeepersKeypersTeam.toJson())
                .add(theEscapePeasTeam.toJson())
        ),
)
val sherUnlockTeamAdded = TeamAdded(
    gameId = escapeCampId,
    version = 5,
    addedAt = sherUnlockTeamAddedAt,
    addedBy = sherUnlockTeamId,
    event = genericObjectMapper.createObjectNode()
        .put("id", "981e1b04-ecc6-48b3-b750-58f20faa5e05")
        .put("updatedBy", "a6e05314-2af2-43c7-a274-d024cf053b42")
        .put("updatedAt", "2023-06-15T13:35")
        .set(
            "teams",
            genericObjectMapper.createArrayNode()
                .add(lockedAndLoadedTeam.toJson())
                .add(jeepersKeypersTeam.toJson())
                .add(theEscapePeasTeam.toJson())
                .add(sherUnlockTeam.toJson())
        ),
)

val escapeCampStarted =
    GameEvent.GameStarted(gameId = escapeCampId, version = 6, startedAt = sherUnlockTeamAddedAt, startedBy = sherUnlockTeamId)

val eventsAfterEscapeCampStarted = listOf(escapeCampCreated, lockedAndLoadedTeamAdded, jeepersKeypersTeamAdded, theEscapePeasTeamAdded, sherUnlockTeamAdded, escapeCampStarted)

val lockedAndLoadedFirstRiddleAssigned =
    GameEvent.NextTeamRiddleAssigned(
        gameId = escapeCampId,
        version = 7,
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
                    .add(theEscapePeasTeam.toJson())
                    .add(sherUnlockTeam.toJson()))
    )

val eventsAfterLockedAndLoadedFirstRiddleAssigned = listOf(escapeCampCreated, lockedAndLoadedTeamAdded, jeepersKeypersTeamAdded, theEscapePeasTeamAdded, sherUnlockTeamAdded, escapeCampStarted, lockedAndLoadedFirstRiddleAssigned)

val sherUnlockFirstRiddleAssigned =
    GameEvent.NextTeamRiddleAssigned(
        gameId = escapeCampId,
        version = 8,
        assignedAt = sherUnlockFirstRiddleAssignedAt,
        assignedBy = sherUnlockTeamId,
        event = genericObjectMapper.createObjectNode()
            .put("id", "981e1b04-ecc6-48b3-b750-58f20faa5e05")
            .put("updatedAt", "2023-06-15T13:36:33")
            .put("updatedBy", "a6e05314-2af2-43c7-a274-d024cf053b42")
            .set(
                "teams",
                genericObjectMapper.createArrayNode()
                    .add(lockedAndLoadedTeamAfterFirstRiddleAssigned.toJson())
                    .add(jeepersKeypersTeam.toJson())
                    .add(theEscapePeasTeam.toJson())
                    .add(sherUnlockTeamAfterFirstRiddleAssigned.toJson()))
    )

val theEscapePeasFirstRiddleAssigned =
    GameEvent.NextTeamRiddleAssigned(
        gameId = escapeCampId,
        version = 9,
        assignedAt = theEscapePeasFirstRiddleAssignedAt,
        assignedBy = theEscapePeasTeamId,
        event = genericObjectMapper.createObjectNode()
            .put("id", "981e1b04-ecc6-48b3-b750-58f20faa5e05")
            .put("updatedAt", "2023-06-15T13:36:45")
            .put("updatedBy", "91700c93-10f9-474e-8176-811598a9aaef")
            .set(
                "teams",
                genericObjectMapper.createArrayNode()
                    .add(lockedAndLoadedTeamAfterFirstRiddleAssigned.toJson())
                    .add(jeepersKeypersTeam.toJson())
                    .add(theEscapePeasTeamAfterFirstRiddleAssigned.toJson())
                    .add(sherUnlockTeamAfterFirstRiddleAssigned.toJson()))
    )

val jeepersKeypersFirstRiddleAssigned =
    GameEvent.NextTeamRiddleAssigned(
        gameId = escapeCampId,
        version = 9,
        assignedAt = jeepersKeypersFirstRiddleAssignedAt,
        assignedBy = jeepersKeypersTeamId,
        event = genericObjectMapper.createObjectNode()
            .put("id", "981e1b04-ecc6-48b3-b750-58f20faa5e05")
            .put("updatedAt", "2023-06-15T13:36:45")
            .put("updatedBy", "3a66dce7-ca96-4cd0-af56-6bd7e082edd5")
            .set(
                "teams",
                genericObjectMapper.createArrayNode()
                    .add(lockedAndLoadedTeamAfterFirstRiddleAssigned.toJson())
                    .add(jeepersKeypersTeamAfterFirstRiddleAssigned.toJson())
                    .add(theEscapePeasTeamAfterFirstRiddleAssigned.toJson())
                    .add(sherUnlockTeamAfterFirstRiddleAssigned.toJson()))
    )

val eventsAfterAllFirstRiddleAssigned = listOf(escapeCampCreated, lockedAndLoadedTeamAdded, jeepersKeypersTeamAdded, theEscapePeasTeamAdded, sherUnlockTeamAdded, escapeCampStarted, lockedAndLoadedFirstRiddleAssigned, sherUnlockFirstRiddleAssigned, theEscapePeasFirstRiddleAssigned, jeepersKeypersFirstRiddleAssigned)

val jeepersKeypersTeamFirstRiddleSolved =
    GameEvent.RiddleSolved(
        gameId = escapeCampId,
        version = 10,
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
                    .add(theEscapePeasTeamAfterFirstRiddleAssigned.toJson())
                    .add(sherUnlockTeamAfterFirstRiddleAssigned.toJson()))
    )