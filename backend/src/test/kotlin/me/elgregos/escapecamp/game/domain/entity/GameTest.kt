package me.elgregos.escapecamp.game.domain.entity

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.util.*
import kotlin.test.assertEquals

class GameTest {

    @ParameterizedTest
    @ValueSource(ints = [1, 2, 3, 4])
    fun `should add team to game`(numberOfTeam: Int) {
        val game = Game()
        (1..numberOfTeam)
            .map { game.addTeam(UUID.randomUUID(), "Team${it}") }

        assertEquals(numberOfTeam, game.teams().size)
    }

}