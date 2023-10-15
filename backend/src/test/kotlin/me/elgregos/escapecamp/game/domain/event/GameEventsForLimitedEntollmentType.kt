package me.elgregos.escapecamp.game.domain.event

import me.elgregos.escapecamp.config.security.organizerId
import me.elgregos.escapecamp.game.domain.entity.*
import me.elgregos.escapecamp.game.domain.event.GameEvent.*
import me.elgregos.reakteves.libs.genericObjectMapper


val escapeCampCreated = GameCreated(
    gameId = escapeCampId,
    createdAt = escapeCampCreatedAt,
    createdBy = organizerId,
    event = genericObjectMapper.createObjectNode()
        .put("id", "$escapeCampId")
        .put("createdAt", "$escapeCampCreatedAt")
        .put("createdBy", "$organizerId")
        .put("enrollmentType", EnrollmentType.LIMITED_TO_RIDDLE_NUMBER.name)
        .set("riddles", genericObjectMapper.valueToTree(riddles))
)

val eventsAfterLockedAndLoadedAdded = listOf(escapeCampCreated, lockedAndLoadedContestantEnrolled)

val eventsAfterAllContestantEnrolled =
    listOf(escapeCampCreated, lockedAndLoadedContestantEnrolled, jeepersKeypersContestantEnrolled)

val escapeCampStarted = GameStarted(
        gameId = escapeCampId,
        version = 4,
        startedAt = jeepersKeypersContestantEnrolledAt,
        startedBy = jeepersKeypersContestantId
    )

val eventsAfterEscapeCampStarted =
    listOf(escapeCampCreated, lockedAndLoadedContestantEnrolled, jeepersKeypersContestantEnrolled, escapeCampStarted)

val lockedAndLoadedFirstRiddleAssigned = NextContestantRiddleAssigned(
    gameId = escapeCampId,
    version = 5,
    assignedAt = lockedAndLoadedFirstRiddleAssignedAt,
    assignedBy = lockedAndLoadedContestantId,
    event = genericObjectMapper.createObjectNode()
        .put("id", "981e1b04-ecc6-48b3-b750-58f20faa5e05")
        .put("updatedAt", "2023-06-15T13:36:12")
        .put("updatedBy", "0bfce65c-dff9-4f2e-8e8f-11ed6151b205")
        .set(
            "contestants",
            genericObjectMapper.createArrayNode()
                .add(lockedAndLoadedContestantAfterFirstRiddleAssigned.toJson())
                .add(jeepersKeypersContestant.toJson())
        )
)

val eventsAfterLockedAndLoadedFirstRiddleAssigned = listOf(
    escapeCampCreated,
    lockedAndLoadedContestantEnrolled,
    jeepersKeypersContestantEnrolled,
    escapeCampStarted,
    lockedAndLoadedFirstRiddleAssigned
)

val jeepersKeypersFirstRiddleAssigned = NextContestantRiddleAssigned(
        gameId = escapeCampId,
        version = 6,
        assignedAt = jeepersKeypersFirstRiddleAssignedAt,
        assignedBy = jeepersKeypersContestantId,
        event = genericObjectMapper.createObjectNode()
            .put("id", "981e1b04-ecc6-48b3-b750-58f20faa5e05")
            .put("updatedAt", "2023-06-15T13:37:04")
            .put("updatedBy", "3a66dce7-ca96-4cd0-af56-6bd7e082edd5")
            .set(
                "contestants",
                genericObjectMapper.createArrayNode()
                    .add(lockedAndLoadedContestantAfterFirstRiddleAssigned.toJson())
                    .add(jeepersKeypersContestantAfterFirstRiddleAssigned.toJson())
            )
    )

val eventsAfterAllFirstRiddleAssigned = listOf(
    escapeCampCreated,
    lockedAndLoadedContestantEnrolled,
    jeepersKeypersContestantEnrolled,
    escapeCampStarted,
    lockedAndLoadedFirstRiddleAssigned,
    jeepersKeypersFirstRiddleAssigned
)

val jeepersKeypersFirstRiddleSolved = RiddleSolved(
        gameId = escapeCampId,
        version = 7,
        solvedAt = jeepersKeypersFirstRiddleSolvedAt,
        solvedBy = jeepersKeypersContestantId,
        event = genericObjectMapper.createObjectNode()
            .put("id", "981e1b04-ecc6-48b3-b750-58f20faa5e05")
            .put("updatedAt", "2023-06-15T13:41:45")
            .put("updatedBy", "3a66dce7-ca96-4cd0-af56-6bd7e082edd5")
            .set(
                "contestants",
                genericObjectMapper.createArrayNode()
                    .add(lockedAndLoadedContestantAfterFirstRiddleAssigned.toJson())
                    .add(jeepersKeypersContestantAfterFirstRiddleSolved.toJson())
            )
    )

val eventsAfterJeepersKeypersFirstRiddleSolved = listOf(
    escapeCampCreated,
    lockedAndLoadedContestantEnrolled,
    jeepersKeypersContestantEnrolled,
    escapeCampStarted,
    lockedAndLoadedFirstRiddleAssigned,
    jeepersKeypersFirstRiddleAssigned,
    jeepersKeypersFirstRiddleSolved
)

val secondRiddleUnlocked = NextRiddleUnlocked(
    gameId = escapeCampId,
    version = 8,
    unlockedAt = secondRiddleUnlockedAt,
    unlockedBy = organizerId,
    unlockedRiddle = 1
)

val eventsAfterSecondRiddleUnlocked = listOf(
    unlimitedEnrollmentEscapeCampCreated,
    lockedAndLoadedContestantEnrolled,
    jeepersKeypersContestantEnrolled,
    escapeCampStarted,
    lockedAndLoadedFirstRiddleAssigned,
    jeepersKeypersFirstRiddleAssigned,
    jeepersKeypersFirstRiddleSolved,
    secondRiddleUnlocked
)

val jeepersKeypersSecondRiddleAssigned = NextContestantRiddleAssigned(
        gameId = escapeCampId,
        version = 8,
        assignedAt = jeepersKeypersSecondRiddleAssignedAt,
        assignedBy = jeepersKeypersContestantId,
        event = genericObjectMapper.createObjectNode()
            .put("id", "981e1b04-ecc6-48b3-b750-58f20faa5e05")
            .put("updatedAt", "2023-06-15T13:42:28")
            .put("updatedBy", "3a66dce7-ca96-4cd0-af56-6bd7e082edd5")
            .set(
                "contestants",
                genericObjectMapper.createArrayNode()
                    .add(lockedAndLoadedContestantAfterFirstRiddleAssigned.toJson())
                    .add(jeepersKeypersContestantAfterSecondRiddleAssigned.toJson())
            )
    )

val lockedAndLoadedSecondRiddleAssigned = NextContestantRiddleAssigned(
        gameId = escapeCampId,
        version = 9,
        assignedAt = lockedAndLoadedSecondRiddleAssignedAt,
        assignedBy = lockedAndLoadedContestantId,
        event = genericObjectMapper.createObjectNode()
            .put("id", "981e1b04-ecc6-48b3-b750-58f20faa5e05")
            .put("updatedAt", "2023-06-15T13:44:02")
            .put("updatedBy", "0bfce65c-dff9-4f2e-8e8f-11ed6151b205")
            .set(
                "contestants",
                genericObjectMapper.createArrayNode()
                    .add(lockedAndLoadedContestantAfterSecondRiddleAssigned.toJson())
                    .add(jeepersKeypersContestantAfterSecondRiddleAssigned.toJson())
            )
    )

val eventsAfterLockedAndLoadedSecondRiddleAssigned = listOf(
    escapeCampCreated,
    lockedAndLoadedContestantEnrolled,
    jeepersKeypersContestantEnrolled,
    escapeCampStarted,
    lockedAndLoadedFirstRiddleAssigned,
    jeepersKeypersFirstRiddleAssigned,
    jeepersKeypersFirstRiddleSolved,
    jeepersKeypersSecondRiddleAssigned,
    lockedAndLoadedSecondRiddleAssigned
)

val jeepersKeypersSecondRiddleSolved = RiddleSolved(
        gameId = escapeCampId,
        version = 10,
        solvedAt = jeepersKeypersSecondRiddleSolvedAt,
        solvedBy = jeepersKeypersContestantId,
        event = genericObjectMapper.createObjectNode()
            .put("id", "981e1b04-ecc6-48b3-b750-58f20faa5e05")
            .put("updatedAt", "2023-06-15T13:47:36")
            .put("updatedBy", "3a66dce7-ca96-4cd0-af56-6bd7e082edd5")
            .set(
                "contestants",
                genericObjectMapper.createArrayNode()
                    .add(lockedAndLoadedContestantAfterSecondRiddleAssigned.toJson())
                    .add(jeepersKeypersContestantAfterSecondRiddleSolved.toJson())
            )
    )

val escapeCampWinnerAnnounced = WinnerAnnounced(
        gameId = escapeCampId,
        version = 11,
        definedAt = jeepersKeypersSecondRiddleSolvedAt,
        definedBy = jeepersKeypersContestantId,
        event = genericObjectMapper.createObjectNode()
            .put("id", "981e1b04-ecc6-48b3-b750-58f20faa5e05")
            .put("winner", "3a66dce7-ca96-4cd0-af56-6bd7e082edd5"))



val eventsAfterWinnerAnnounced = listOf(
    escapeCampCreated,
    lockedAndLoadedContestantEnrolled,
    jeepersKeypersContestantEnrolled,
    escapeCampStarted,
    lockedAndLoadedFirstRiddleAssigned,
    jeepersKeypersFirstRiddleAssigned,
    jeepersKeypersFirstRiddleSolved,
    jeepersKeypersSecondRiddleAssigned,
    lockedAndLoadedSecondRiddleAssigned,
    jeepersKeypersSecondRiddleSolved,
    escapeCampWinnerAnnounced
)

val lockedAndLoadedSecondRiddleSolved = RiddleSolved(
        gameId = escapeCampId,
        version = 12,
        solvedAt = lockedAndLoadedSecondRiddleSolvedAt,
        solvedBy = lockedAndLoadedContestantId,
        event = genericObjectMapper.createObjectNode()
            .put("id", "981e1b04-ecc6-48b3-b750-58f20faa5e05")
            .put("updatedAt", "2023-06-15T13:50:05")
            .put("updatedBy", "0bfce65c-dff9-4f2e-8e8f-11ed6151b205")
            .set(
                "contestants",
                genericObjectMapper.createArrayNode()
                    .add(lockedAndLoadedContestantAfterSecondRiddleSolved.toJson())
                    .add(jeepersKeypersContestantAfterSecondRiddleSolved.toJson())
            )
    )
