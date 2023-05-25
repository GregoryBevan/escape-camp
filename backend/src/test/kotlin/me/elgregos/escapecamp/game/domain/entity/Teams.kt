package me.elgregos.escapecamp.game.domain.entity

import java.util.*

val lockedAndLoadedTeamId = UUID.fromString("0bfce65c-dff9-4f2e-8e8f-11ed6151b205")!!
const val lockedAndLoadedTeamName = "Locked and Loaded"
val lockedAndLoadedTeam = Team(lockedAndLoadedTeamId, lockedAndLoadedTeamName)

val jeepersKeypersTeamId = UUID.fromString("3a66dce7-ca96-4cd0-af56-6bd7e082edd5")!!
val jeepersKeypersTeam = Team(jeepersKeypersTeamId, "Jeepers Keypers")

val theEscapePeasTeamId = UUID.fromString("91700c93-10f9-474e-8176-811598a9aaef")!!
val theEscapePeasTeam = Team(theEscapePeasTeamId, "The Escape Peas")

val sherUnlockTeamId = UUID.fromString("a6e05314-2af2-43c7-a274-d024cf053b42")!!
val sherUnlockTeam = Team(sherUnlockTeamId, "Sher-unlock")