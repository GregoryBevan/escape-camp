package me.elgregos.escapecamp.game.domain.entity

import java.util.*

val lockedAndLoadedContestantId:UUID = UUID.fromString("0bfce65c-dff9-4f2e-8e8f-11ed6151b205")
const val lockedAndLoadedContestantName = "Locked and Loaded"
val lockedAndLoadedContestant = Contestant(lockedAndLoadedContestantId, lockedAndLoadedContestantName)

val jeepersKeypersContestantId:UUID = UUID.fromString("3a66dce7-ca96-4cd0-af56-6bd7e082edd5")
const val jeepersKeypersContestantName = "Jeepers Keypers"
val jeepersKeypersContestant = Contestant(jeepersKeypersContestantId, jeepersKeypersContestantName)

val unknownContestantId: UUID = UUID.fromString("4f5ddf39-4a5c-4317-b4a6-db2d6c50dd18")

val lockedAndLoadedContestantAfterFirstRiddleAssigned = lockedAndLoadedContestant.copy(riddles = listOf(lockedAndLoadedFirstRiddle))

val jeepersKeypersContestantAfterFirstRiddleAssigned = jeepersKeypersContestant.copy(riddles = listOf(jeepersKeypersFirstRiddle))

val jeepersKeypersContestantAfterFirstRiddleSolved =  jeepersKeypersContestant.copy(riddles = listOf(jeepersKeypersFirstSolvedRiddle))
val jeepersKeypersContestantAfterSecondRiddleAssigned =  jeepersKeypersContestant.copy(riddles = listOf(jeepersKeypersFirstSolvedRiddle, jeepersKeypersSecondRiddle))

val lockedAndLoadedContestantAfterFirstRiddleSolved = lockedAndLoadedContestant.copy(riddles = listOf(lockedAndLoadedFirstSolvedRiddle))
val lockedAndLoadedContestantAfterSecondRiddleAssigned = lockedAndLoadedContestant.copy(riddles = listOf(lockedAndLoadedFirstSolvedRiddle, lockedAndLoadedSecondRiddle))

val jeepersKeypersContestantAfterSecondRiddleSolved =  jeepersKeypersContestant.copy(riddles = listOf(jeepersKeypersFirstSolvedRiddle, jeepersKeypersSecondSolvedRiddle))

val lockedAndLoadedContestantAfterSecondRiddleSolved =  lockedAndLoadedContestant.copy(riddles = listOf(lockedAndLoadedFirstSolvedRiddle, lockedAndLoadedSecondSolvedRiddle))



