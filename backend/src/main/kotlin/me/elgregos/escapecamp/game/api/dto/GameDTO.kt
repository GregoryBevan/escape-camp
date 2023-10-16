package me.elgregos.escapecamp.game.api.dto

import me.elgregos.escapecamp.game.domain.entity.EnrollmentType

data class GameDTO(val enrollmentType: EnrollmentType= EnrollmentType.LIMITED_TO_RIDDLE_NUMBER)
