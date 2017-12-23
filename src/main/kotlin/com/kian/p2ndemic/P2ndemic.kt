package com.kian.p2ndemic

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

typealias Card = String
typealias Cards = List<Card>

val mapper = jacksonObjectMapper()

data class P2ndemic(val deck: List<Cards> = emptyList(),
                    val discard: Cards = emptyList(),
                    val epidemics: Int = 0,
                    val totalCardsNum: Int = 0) {
    fun draw(card: String) : P2ndemic {
        var newDeck = deck
        var firstDeck = if(deck.isEmpty()) emptyList() else deck[0]
        if (!firstDeck.isEmpty()) {
            if (!firstDeck.contains(card)) {
                throw UnsupportedOperationException("That should not be possible!")
            }
            firstDeck -= card
            newDeck = deck.subList(1, deck.size)
            if (!firstDeck.isEmpty()) {
                newDeck = newDeck.reversed().plusElement(firstDeck).reversed()
            }
        }
        return P2ndemic(newDeck, discard + card, epidemics)
    }

    fun epidemic() : P2ndemic {
        val newTop = deck.reversed().plusElement(discard).reversed()
        return P2ndemic(newTop, listOf(), epidemics + 1)
    }


    fun printOdds() {
        val cardsToDraw = cardDrawNum(epidemics)
        var cardsDrawn = 0
        var currentList = 0
        while (cardsDrawn < cardsToDraw) {
            if (currentList >= deck.size) {
                break
            }
            println(oddsNextDraw(deck[currentList], (cardsToDraw - cardsDrawn)))
            cardsDrawn += minOf(deck[currentList].size, cardsToDraw)
            currentList++
        }
    }

    fun pandemic(input: String): P2ndemic {
        val inp = input.split(" ")
        return when (inp[0].toUpperCase()) {
            "EPIDEMIC" -> epidemic()
            "REMOVE" -> copy(discard = (discard - inp[1]))
            "EXPORT" -> {
                println(mapper.writeValueAsString(this))
                this
            }
            "IMPORT" -> mapper.readValue(inp[1].toLowerCase())
            "LIST" -> {
                println("Discard = " + discard.toString())
                println("Deck = " + deck.toString())
                println("Epidemics = " + epidemics)
                println("Cards Drawn = " + cardDrawNum(epidemics))
                this
            }
            else -> {
                draw(input)
            }
        }
    }


    fun cardDrawNum(epidemics: Int): Int {
        return when {
            epidemics <= 2 -> 2
            epidemics <= 4 -> 3
            epidemics <= 6 -> 4
            else -> 5
        }
    }
}

fun runGame() {
    var game = P2ndemic()
    while (true) {
        try {
            println("Enter your card or epidemic:")
            val input: String = readLine() ?: continue
            game = game.pandemic(input)
            game.printOdds()
        } catch (e: Exception) {
            e.printStackTrace()
            println("Try Again")
        }
    }
}

fun main(args: Array<String>) {
    runGame()
}

