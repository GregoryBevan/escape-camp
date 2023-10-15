package me.elgregos.escapecamp.game.domain.event

import me.elgregos.escapecamp.game.domain.entity.*
import me.elgregos.escapecamp.game.domain.event.GameEvent.ContestantEnrolled
import me.elgregos.reakteves.libs.genericObjectMapper


val lockedAndLoadedContestantEnrolled = ContestantEnrolled(
    gameId = escapeCampId,
    version = 2,
    enrolledAt = lockedAndLoadedContestantEnrolledAt,
    enrolledBy = lockedAndLoadedContestantId,
    event = genericObjectMapper.createObjectNode()
        .put("id", "981e1b04-ecc6-48b3-b750-58f20faa5e05")
        .put("updatedBy", "0bfce65c-dff9-4f2e-8e8f-11ed6151b205")
        .put("updatedAt", "2023-06-15T13:31")
        .set(
            "contestants",
            genericObjectMapper.createArrayNode()
                .add(lockedAndLoadedContestant.toJson())
        )
)

val jeepersKeypersContestantEnrolled = ContestantEnrolled(
    gameId = escapeCampId,
    version = 3,
    enrolledAt = jeepersKeypersContestantEnrolledAt,
    enrolledBy = jeepersKeypersContestantId,
    event = genericObjectMapper.createObjectNode()
        .put("id", "981e1b04-ecc6-48b3-b750-58f20faa5e05")
        .put("updatedBy", "3a66dce7-ca96-4cd0-af56-6bd7e082edd5")
        .put("updatedAt", "2023-06-15T13:35")
        .set(
            "contestants",
            genericObjectMapper.createArrayNode()
                .add(lockedAndLoadedContestant.toJson())
                .add(jeepersKeypersContestant.toJson())
        ),
)

val sherUnlockContestantEnrolled = ContestantEnrolled(
    gameId = escapeCampId,
    version = 4,
    enrolledAt = sherUnlockContestantEnrolledAt,
    enrolledBy = sherUnlockContestantId,
    event = genericObjectMapper.createObjectNode()
        .put("id", "981e1b04-ecc6-48b3-b750-58f20faa5e05")
        .put("updatedBy", "cbedf28e-eca2-49bc-b201-cc94f76ef587")
        .put("updatedAt", "2023-06-15T13:36")
        .set(
            "contestants",
            genericObjectMapper.createArrayNode()
                .add(lockedAndLoadedContestant.toJson())
                .add(jeepersKeypersContestant.toJson())
                .add(sherUnlockContestant.toJson())
        ),
)

