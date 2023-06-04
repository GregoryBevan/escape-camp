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

val lockedAndLoadedTeamAddedAt: LocalDateTime = LocalDateTime.of(2023, 6, 15, 13, 31)

val lockedAndLoadedTeamAdded = TeamAdded(
    gameId = escapeCampId,
    version = 2,
    createdAt = lockedAndLoadedTeamAddedAt,
    createdBy = lockedAndLoadedTeamId,
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
val jeepersKeypersTeamAddedAt: LocalDateTime = LocalDateTime.of(2023, 6, 15, 13, 32)

val jeepersKeypersTeamAdded = TeamAdded(
    gameId = escapeCampId,
    version = 3,
    createdAt = jeepersKeypersTeamAddedAt,
    createdBy = jeepersKeypersTeamId,
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
    createdAt = theEscapePeasTeamAddedAt,
    createdBy = theEscapePeasTeamId,
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

val sherUnlockTeamAddedAt: LocalDateTime = LocalDateTime.of(2023, 6, 15, 13, 35)

val sherUnlockTeamAdded = TeamAdded(
    gameId = escapeCampId,
    version = 5,
    createdAt = sherUnlockTeamAddedAt,
    createdBy = sherUnlockTeamId,
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
    GameEvent.GameStarted(gameId = escapeCampId, version = 6, createdAt = sherUnlockTeamAddedAt, createdBy = sherUnlockTeamId)
