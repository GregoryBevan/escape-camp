package me.elgregos.escapecamp.game.domain.entity

import me.elgregos.escapecamp.config.security.organizerId
import java.time.LocalDateTime
import java.util.*

val unknownGameId: UUID = UUID.fromString("07c905e7-8179-4b59-a65a-510a4e1de4d3")
val escapeCampId: UUID = UUID.fromString("981e1b04-ecc6-48b3-b750-58f20faa5e05")
val escapeCampCreatedAt: LocalDateTime = LocalDateTime.of(2023, 5, 19, 21, 40, 18)
val lockedAndLoadedContestantEnrolledAt: LocalDateTime = LocalDateTime.of(2023, 6, 15, 13, 31)
val jeepersKeypersContestantEnrolledAt: LocalDateTime = LocalDateTime.of(2023, 6, 15, 13, 35)
val sherUnlockContestantEnrolledAt: LocalDateTime = LocalDateTime.of(2023, 6, 15, 13, 36)

val escapeCamp = Game(escapeCampId, createdAt = escapeCampCreatedAt, createdBy = organizerId, riddles = riddles)
val unlimitedEnrollmentEscapeCamp = Game(escapeCampId, createdAt = escapeCampCreatedAt, createdBy = organizerId, riddles = riddles, enrollmentType = EnrollmentType.UNLIMITED)

val escapeCampAfterLockedAndLoadedContestantEnrolled =
    escapeCamp.copy(
        version = 2,
        updatedAt = lockedAndLoadedContestantEnrolledAt,
        updatedBy = lockedAndLoadedContestantId,
        contestants = listOf(lockedAndLoadedContestant)
    )

val escapeCampAfterJeepersKeypersContestantEnrolled =
    escapeCamp.copy(
        version = 3,
        updatedAt = jeepersKeypersContestantEnrolledAt,
        updatedBy = jeepersKeypersContestantId,
        contestants = listOf(lockedAndLoadedContestant, jeepersKeypersContestant)
    )

val unlimitedEnrollmentEscapeCampAfterJeepersKeypersContestantEnrolled =
    escapeCampAfterJeepersKeypersContestantEnrolled.copy(
        enrollmentType = EnrollmentType.UNLIMITED
    )

val escapeCampAfterGameStarted =
    escapeCampAfterJeepersKeypersContestantEnrolled.copy(
        version = 4,
        startedAt = jeepersKeypersContestantEnrolledAt
    )

val escapeCampWithFirstRiddleUnlocked =
    unlimitedEnrollmentEscapeCampAfterJeepersKeypersContestantEnrolled.copy(
        version = 4,
        currentRiddle = 0,
        updatedAt = firstRiddleUnlockedAt,
        updatedBy = organizerId,
        startedAt = firstRiddleUnlockedAt
    )

val unlimitedEnrollmentEscapeCampAfterGameStarted =
    escapeCampWithFirstRiddleUnlocked.copy(
        version = 4,
        startedAt = jeepersKeypersContestantEnrolledAt
    )

val escapeCampAfterLockedAndLoadedFirstRiddleAssigned =
    escapeCampAfterGameStarted.copy(
        version = 5,
        updatedAt = lockedAndLoadedFirstRiddleAssignedAt,
        updatedBy = lockedAndLoadedContestantId,
        contestants = listOf(lockedAndLoadedContestantAfterFirstRiddleAssigned, jeepersKeypersContestant)
    )

val unlimitedEnrollmentEscapeCampAfterLockedAndLoadedFirstRiddleAssigned =
    unlimitedEnrollmentEscapeCampAfterGameStarted.copy(
        version = 5,
        updatedAt = lockedAndLoadedFirstRiddleAssignedAt,
        updatedBy = lockedAndLoadedContestantId,
        contestants = listOf(lockedAndLoadedContestantAfterFirstRiddleAssigned, jeepersKeypersContestant)
    )

val escapeCampAfterAllFirstRiddleAssigned =
    escapeCampAfterLockedAndLoadedFirstRiddleAssigned.copy(
        version = 6,
        updatedAt = jeepersKeypersFirstRiddleAssignedAt,
        updatedBy = jeepersKeypersContestantId,
        contestants = listOf( lockedAndLoadedContestantAfterFirstRiddleAssigned, jeepersKeypersContestantAfterFirstRiddleAssigned)
    )

val escapeCampAfterJeepersKeypersFirstRiddleSolved =
    escapeCampAfterLockedAndLoadedFirstRiddleAssigned.copy(
        version = 7,
        updatedAt = jeepersKeypersFirstRiddleSolvedAt,
        updatedBy = jeepersKeypersContestantId,
        contestants = listOf(lockedAndLoadedContestantAfterFirstRiddleAssigned, jeepersKeypersContestantAfterFirstRiddleSolved)
    )

val unlimitedEnrollmentEscapeCampAfterJeepersKeypersFirstRiddleSolved =
    unlimitedEnrollmentEscapeCampAfterLockedAndLoadedFirstRiddleAssigned.copy(
        version = 7,
        updatedAt = jeepersKeypersFirstRiddleSolvedAt,
        updatedBy = jeepersKeypersContestantId,
        contestants = listOf(lockedAndLoadedContestantAfterFirstRiddleAssigned, jeepersKeypersContestantAfterFirstRiddleSolved)
    )

val escapeCampAfterJeepersKeypersSecondRiddleAssigned =
    escapeCampAfterJeepersKeypersFirstRiddleSolved.copy(
        version = 8,
        updatedAt = jeepersKeypersSecondRiddleAssignedAt,
        updatedBy = jeepersKeypersContestantId,
        contestants = listOf(lockedAndLoadedContestantAfterFirstRiddleAssigned, jeepersKeypersContestantAfterSecondRiddleAssigned)
    )

val escapeCampAfterLockedAndLoadedFirstRiddleSolved =
    escapeCampAfterGameStarted.copy(
        version = 9,
        updatedAt = lockedAndLoadedFirstRiddleSolvedAt,
        updatedBy = lockedAndLoadedContestantId,
        contestants = listOf(lockedAndLoadedContestantAfterFirstRiddleSolved, jeepersKeypersContestantAfterSecondRiddleAssigned)
    )

val unlimitedEnrollmentEscapeCampAfterLockedAndLoadedFirstRiddleSolved =
    unlimitedEnrollmentEscapeCampAfterJeepersKeypersFirstRiddleSolved.copy(
        version = 9,
        updatedAt = lockedAndLoadedFirstRiddleSolvedAt,
        updatedBy = lockedAndLoadedContestantId,
        contestants = listOf(lockedAndLoadedContestantAfterFirstRiddleSolved, jeepersKeypersContestantAfterSecondRiddleAssigned)
    )

val unlimitedEscapeCampWithSecondRiddleUnlocked =
    unlimitedEnrollmentEscapeCampAfterLockedAndLoadedFirstRiddleSolved.copy(
        version = 10,
        currentRiddle = 1,
        updatedAt = secondRiddleUnlockedAt,
        updatedBy = organizerId,
    )

val escapeCampAfterLockedAndLoadedSecondRiddleAssigned =
    escapeCampAfterGameStarted.copy(
        version = 10,
        updatedAt = lockedAndLoadedSecondRiddleAssignedAt,
        updatedBy = lockedAndLoadedContestantId,
        contestants = listOf(lockedAndLoadedContestantAfterSecondRiddleAssigned, jeepersKeypersContestantAfterSecondRiddleAssigned)
    )

val escapeCampAfterJeepersKeypersSecondRiddleSolved =
    unlimitedEnrollmentEscapeCampAfterGameStarted.copy(
        version = 11,
        updatedAt = jeepersKeypersSecondRiddleSolvedAt,
        updatedBy = jeepersKeypersContestantId,
        contestants = listOf(lockedAndLoadedContestantAfterSecondRiddleAssigned, jeepersKeypersContestantAfterSecondRiddleSolved)
    )

val escapeCampWithWinner =
    unlimitedEnrollmentEscapeCampAfterGameStarted.copy(
        version = 12,
        updatedAt = jeepersKeypersSecondRiddleSolvedAt,
        updatedBy = jeepersKeypersContestantId,
        contestants = listOf(lockedAndLoadedContestantAfterSecondRiddleAssigned, jeepersKeypersContestantAfterSecondRiddleSolved),
        winner = jeepersKeypersContestantId
    )

val escapeCampAfterLockAndLoadedSecondRiddleSolved =
    unlimitedEnrollmentEscapeCampAfterGameStarted.copy(
        version = 13,
        updatedAt = lockedAndLoadedSecondRiddleSolvedAt,
        updatedBy = lockedAndLoadedContestantId,
        contestants = listOf(lockedAndLoadedContestantAfterSecondRiddleSolved, jeepersKeypersContestantAfterSecondRiddleSolved),
        winner = jeepersKeypersContestantId
    )
