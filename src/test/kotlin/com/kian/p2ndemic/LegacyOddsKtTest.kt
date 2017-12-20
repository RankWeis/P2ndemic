package com.kian.p2ndemic

import org.jetbrains.spek.api.Spek
import org.junit.runner.RunWith
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.junit.platform.runner.JUnitPlatform
import org.assertj.core.api.Assertions.assertThat

@RunWith(JUnitPlatform::class)
object LegacyOddsSpek: Spek({
    describe("Odds on single card") {
        it("Can find simple odds") {
            assertThat(findOddsForCard(1, 1, 1)).isEqualTo(1.0)
            assertThat(findOddsForCard(2, 2, 2)).isEqualTo(1.0)

        }
         it("Can find 50/50 odds") {
             assertThat(findOddsForCard(1, 2, 1)).isEqualTo(.5)
             assertThat(findOddsForCard(2, 4, 1)).isEqualTo(.5)
         }

        it("Can find 1/4 odds") {
            assertThat(findOddsForCard(1, 4, 1)).isEqualTo(.25)
        }

        it("Can find 1/3 odds") {
            assertThat(findOddsForCard(1, 3, 1)).isGreaterThan(.33).isLessThan(.34)
        }

        it("Can find complex odds") {
            assertThat(findOddsForCard(2, 14, 2)).isGreaterThan(.27).isLessThan(.28)
        }
        it("Can handle more cards drawn than exist") {
            assertThat(findOddsForCard(1, 1, 2)).isEqualTo(1.0)
        }

    }
    describe ("odds on the next draw") {
        it("Can display odds") {
            val cards = listOf("San Francisco", "Chicago", "Sao Paulo")
            assertThat(oddsNextDraw(cards, 0))
                    .contains("San Francisco=0.66")
                    .contains("Chicago=0.66")
                    .contains("Sao Paulo=0.66")
        }
    }

})
