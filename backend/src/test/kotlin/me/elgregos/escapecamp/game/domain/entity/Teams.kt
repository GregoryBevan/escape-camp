package me.elgregos.escapecamp.game.domain.entity

import java.util.*

val lockedAndLoadedTeamId:UUID = UUID.fromString("0bfce65c-dff9-4f2e-8e8f-11ed6151b205")
const val lockedAndLoadedTeamName = "Locked and Loaded"
val lockedAndLoadedTeam = Team(lockedAndLoadedTeamId, lockedAndLoadedTeamName)

val jeepersKeypersTeamId:UUID = UUID.fromString("3a66dce7-ca96-4cd0-af56-6bd7e082edd5")
const val jeepersKeypersTeamName = "Jeepers Keypers"
val jeepersKeypersTeam = Team(jeepersKeypersTeamId, jeepersKeypersTeamName)

val theEscapePeasTeamId:UUID = UUID.fromString("91700c93-10f9-474e-8176-811598a9aaef")
const val theEscapePeasTeamName = "The Escape Peas"
val theEscapePeasTeam = Team(theEscapePeasTeamId, theEscapePeasTeamName)

val sherUnlockTeamId: UUID = UUID.fromString("a6e05314-2af2-43c7-a274-d024cf053b42")
const val sherUnlockTeamName = "Sher-unlock"
val sherUnlockTeam = Team(sherUnlockTeamId, sherUnlockTeamName)

val unknownTeamId: UUID = UUID.fromString("4f5ddf39-4a5c-4317-b4a6-db2d6c50dd18")

val lockedAndLoadedTeamAfterFirstRiddleAssigned = lockedAndLoadedTeam.copy(riddles = listOf(lockedAndLoadedFirstRiddle))
val sherUnlockTeamAfterFirstRiddleAssigned = sherUnlockTeam.copy(riddles = listOf(sherUnlockFirstRiddle))
val theEscapePeasTeamAfterFirstRiddleAssigned = theEscapePeasTeam.copy(riddles = listOf(theEscapePeasFirstRiddle))
val jeepersKeypersTeamAfterFirstRiddleAssigned = jeepersKeypersTeam.copy(riddles = listOf(jeepersKeypersFirstRiddle))


val jeepersKeypersTeamAfterFirstRiddleSolved =  jeepersKeypersTeam.copy(riddles = listOf(jeepersKeypersFirstSolvedRiddle))
val lockedAndLoadedTeamAfterFirstRiddleSolved = lockedAndLoadedTeam.copy(riddles = listOf(lockedAndLoadedFirstSolvedRiddle))

val jeepersKeypersTeamAfterSecondRiddleAssigned =  jeepersKeypersTeam.copy(riddles = listOf(jeepersKeypersFirstSolvedRiddle, jeepersKeypersSecondRiddle))
