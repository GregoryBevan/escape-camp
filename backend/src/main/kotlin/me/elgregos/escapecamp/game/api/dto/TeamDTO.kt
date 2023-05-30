package me.elgregos.escapecamp.game.api.dto

import jakarta.validation.constraints.NotBlank

data class TeamCreationDTO(
    @NotBlank val name: String
)
