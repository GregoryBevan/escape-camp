package me.elgregos.escapecamp.game.domain.event

import me.elgregos.escapecamp.config.security.organizerId
import me.elgregos.escapecamp.game.domain.entity.*
import me.elgregos.escapecamp.game.domain.event.GameEvent.*
import me.elgregos.reakteves.libs.genericObjectMapper

val unlimitedEnrollmentEscapeCampCreated = GameCreated(
    gameId = escapeCampId,
    createdAt = escapeCampCreatedAt,
    createdBy = organizerId,
    event = genericObjectMapper.createObjectNode()
        .put("id", "$escapeCampId")
        .put("createdAt", "$escapeCampCreatedAt")
        .put("createdBy", "$organizerId")
        .put("enrollmentType", EnrollmentType.UNLIMITED.name)
        .set("riddles", genericObjectMapper.valueToTree(riddles))
)

val eventsAfterAllContestantEnrolledInUnlimitedEnrollmentGame =
    listOf(unlimitedEnrollmentEscapeCampCreated, lockedAndLoadedContestantEnrolled, jeepersKeypersContestantEnrolled)

val firstRiddleUnlocked = NextRiddleUnlocked(
        gameId = escapeCampId,
        version = 4,
        unlockedAt = firstRiddleUnlockedAt,
        unlockedBy = organizerId,
        unlockedRiddle = 0
    )

val unlimitedEnrollmentEscapeCampStarted = GameStarted(
    gameId = escapeCampId,
    version = 5,
    startedAt = firstRiddleUnlockedAt,
    startedBy = organizerId
)

val eventsAfterFirstRiddleUnlocked =
    listOf(
        unlimitedEnrollmentEscapeCampCreated,
        lockedAndLoadedContestantEnrolled,
        jeepersKeypersContestantEnrolled,
        firstRiddleUnlocked,
        unlimitedEnrollmentEscapeCampStarted
    )

val lockedAndLoadedFirstRiddleAssignedInUnlimitedEnrollmentGame = NextContestantRiddleAssigned(
    gameId = escapeCampId,
    version = 6,
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

val eventsAfterLockedAndLoadedFirstRiddleAssignedInUnlimitedEnrollmentGame =
    listOf(
        unlimitedEnrollmentEscapeCampCreated,
        lockedAndLoadedContestantEnrolled,
        jeepersKeypersContestantEnrolled,
        firstRiddleUnlocked,
        unlimitedEnrollmentEscapeCampStarted,
        lockedAndLoadedFirstRiddleAssignedInUnlimitedEnrollmentGame
    )

val jeepersKeypersFirstRiddleAssignedInUnlimitedEnrollmentGame = NextContestantRiddleAssigned(
    gameId = escapeCampId,
    version = 7,
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
                .add(jeepersKeypersContestantAfterFirstRiddleAssignedInUnlimitedEnrollmentGame.toJson())
        )
)
