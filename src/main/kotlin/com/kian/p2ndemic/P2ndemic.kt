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
        var newDeck : List<Cards> = deck
        var firstDeck = if(deck.isEmpty()) emptyList() else deck[0]
        if (!firstDeck.isEmpty()) {
            if (!firstDeck.contains(card)) {
                throw UnsupportedOperationException("That should not be possible!")
            }
            firstDeck -= card
            newDeck =  deck.subList(1, deck.size)
            if (!firstDeck.isEmpty()) {
                newDeck = listOf(firstDeck).plus(newDeck)
            }
        }
        return this.copy(deck = newDeck, discard = discard + card)
    }

    fun epidemic(card: String) : P2ndemic {
        if (card.isBlank()) {
            throw UnsupportedOperationException("add epidemic'd card")
        }
        return P2ndemic(listOf(discard.plus(card)).plus(deck), emptyList(), epidemics + 1)
    }


    fun printOdds() {
        val cardsToDraw = cardDrawNum()
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
            "EPIDEMIC" -> epidemic(inp[1])
            "REMOVE" -> copy(discard = (discard - inp[1]))
            "EXPORT" -> {
                println(mapper.writeValueAsString(this))
                this
            }
            "IMPORT" -> mapper.readValue(inp[1])
            "LIST", "LS" -> {
                println("Discard = " + discard.toString())
                println("Deck = " + deck.toString())
                println("Epidemics = " + epidemics)
                println("Cards Drawn = " + cardDrawNum())
                this
            }
            else -> {
                draw(input)
            }
        }
    }


    private fun cardDrawNum(): Int {
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

