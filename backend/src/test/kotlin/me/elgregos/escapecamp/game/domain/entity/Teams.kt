package me.elgregos.escapecamp.game.domain.entity

import java.util.*

val lockedAndLoadedTeamId:UUID = UUID.fromString("0bfce65c-dff9-4f2e-8e8f-11ed6151b205")
const val lockedAndLoadedTeamName = "Locked and Loaded"
val lockedAndLoadedTeam = Team(lockedAndLoadedTeamId, lockedAndLoadedTeamName)

val jeepersKeypersTeamId:UUID = UUID.fromString("3a66dce7-ca96-4cd0-af56-6bd7e082edd5")
const val jeepersKeypersTeamName = "Jeepers Keypers"
val jeepersKeypersTeam = Team(jeepersKeypersTeamId, jeepersKeypersTeamName)

val unknownTeamId: UUID = UUID.fromString("4f5ddf39-4a5c-4317-b4a6-db2d6c50dd18")

val lockedAndLoadedTeamAfterFirstRiddleAssigned = lockedAndLoadedTeam.copy(riddles = listOf(lockedAndLoadedFirstRiddle))

val jeepersKeypersTeamAfterFirstRiddleAssigned = jeepersKeypersTeam.copy(riddles = listOf(jeepersKeypersFirstRiddle))

val jeepersKeypersTeamAfterFirstRiddleSolved =  jeepersKeypersTeam.copy(riddles = listOf(jeepersKeypersFirstSolvedRiddle))
val jeepersKeypersTeamAfterSecondRiddleAssigned =  jeepersKeypersTeam.copy(riddles = listOf(jeepersKeypersFirstSolvedRiddle, jeepersKeypersSecondRiddle))

val lockedAndLoadedTeamAfterFirstRiddleSolved = lockedAndLoadedTeam.copy(riddles = listOf(lockedAndLoadedFirstSolvedRiddle))
val lockedAndLoadedTeamAfterSecondRiddleAssigned = lockedAndLoadedTeam.copy(riddles = listOf(lockedAndLoadedFirstSolvedRiddle, lockedAndLoadedSecondRiddle))

val jeepersKeypersTeamAfterSecondRiddleSolved =  jeepersKeypersTeam.copy(riddles = listOf(jeepersKeypersFirstSolvedRiddle, jeepersKeypersSecondSolvedRiddle))

val lockedAndLoadedTeamAfterSecondRiddleSolved =  lockedAndLoadedTeam.copy(riddles = listOf(lockedAndLoadedFirstSolvedRiddle, lockedAndLoadedSecondSolvedRiddle))



