package me.elgregos.escapecamp.game.infrastructure.projection

import assertk.assertThat
import assertk.assertions.isEqualTo
import me.elgregos.escapecamp.game.domain.entity.escapeCamp
import me.elgregos.escapecamp.game.infrastructure.projection.GameEntity.Companion.fromGame
import kotlin.test.Test

class GameEntityTest {

    @Test
    fun `should convert game to game entity`() {
        assertThat(fromGame(escapeCamp)).isEqualTo(escapeCampEntity)
    }
}
