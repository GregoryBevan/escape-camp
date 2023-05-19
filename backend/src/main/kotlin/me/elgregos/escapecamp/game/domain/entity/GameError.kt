package me.elgregos.escapecamp.game.domain.entity

sealed class GameError(message: String): Exception(message)

class TeamLimitExceeded : GameError("Number of teams is limited to 4")