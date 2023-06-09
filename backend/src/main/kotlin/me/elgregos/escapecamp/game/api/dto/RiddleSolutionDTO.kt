package me.elgregos.escapecamp.game.api.dto

import jakarta.validation.constraints.NotBlank

data class RiddleSolutionDTO(
    @NotBlank val solution: String
)
