package com.kian.p2ndemic

import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import kotlin.test.assertFailsWith

@RunWith(JUnitPlatform::class)
object P2ndemicSpek : Spek({
    describe("Can calculate pandemic odds") {
        it("Can find the odds") {
            var game = P2ndemic()
                    .draw("a")
                    .draw("b")
                    .draw("c")
                    .epidemic()
                    .draw("a")
            assertThat(game.deck).containsOnly(listOf("b", "c"))
            assertThat(game.discard).containsOnly("a")
            findAllCardOdds(game.deck[0], 2).values
                    .forEach({ assertThat(it).isEqualTo(1.0) })
            game = game
                    .draw("b")
                    .draw("c")
                    .draw("a")
                    .draw("d")
                    .epidemic()
            val odds = findAllCardOdds(game.deck[0], 2)
            assertThat(odds.get("a")).isGreaterThan(odds.get("b")).isGreaterThan(odds.get("c"))
            assertThat(odds.get("b")).isEqualTo(odds.get("c")).isEqualTo(odds.get("d"))
        }

        it("Puts the first deck first") {
            var game = P2ndemic()
                    .draw("a")
                    .draw("b")
                    .draw("c")
                    .epidemic()
                    .draw("a")
                    .draw("b")
                    .epidemic()
                    .draw("a")
            assertThat(findAllCardOdds(game.deck[0], 2)["b"]).isEqualTo(1.0)
        }

        it("Throws an exception if an invalid card is picked") {
            var game = P2ndemic()
                    .draw("a")
                    .draw("b")
                    .draw("c")
            assertFailsWith<UnsupportedOperationException> {
                game.epidemic().draw("d")
            }
            assertThat(game.draw("d").epidemic().draw("d")).isNotNull()
        }
    }
})
