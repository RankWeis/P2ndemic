package com.kian.p2ndemic

typealias Card = String
typealias Cards = List<Card>

data class Gamestate(val deck: List<Cards>, val discard: Cards, val epidemics: Int)

class P2ndemic {
    fun draw(g: Gamestate, card: String) : Gamestate {
        var (topDeck, discard) = g
        var firstDeck = if(topDeck.isEmpty()) emptyList() else topDeck[0]
        if (!firstDeck.isEmpty()) {
            if (!firstDeck.contains(card)) {
                throw UnsupportedOperationException("That should not be possible!")
            }
            firstDeck -= card
            if (firstDeck.isEmpty()) {
                topDeck = topDeck.subList(1, topDeck.size)
            }
        }
        discard += card
        return Gamestate(topDeck, discard, g.epidemics)
    }

    fun epidemic(g: Gamestate) : Gamestate {
        val (topDeck, discard) = g
        val newTop = topDeck.reversed().plusElement(discard).reversed()
        return Gamestate(newTop, listOf(), g.epidemics)
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

fun main(args: Array<String>) {
    val game = P2ndemic()
    var gamestate = Gamestate(emptyList(), emptyList(), 0)
    gamestate = gamestate.copy(deck = gamestate.deck.plusElement(emptyList()))
    while (true) {
        try {
            println("Enter your card or epidemic:")
            val input: String = readLine()?.toUpperCase() ?: continue
            gamestate = pandemic(game, gamestate, input)
        } catch (e: Exception) {
            println(e.message)
            e.printStackTrace()
            println("try again")
        }

        val cardsToDraw = game.cardDrawNum(gamestate.epidemics)
        var cardsDrawn = 0
        var currentList = 0
        while (cardsDrawn < cardsToDraw) {
            if (currentList >= gamestate.deck.size) {
                break
            }
            println(oddsNextDraw(gamestate.deck[currentList], (cardsToDraw - cardsDrawn)))
            cardsDrawn += minOf(gamestate.deck[currentList].size, cardsToDraw)
            currentList++
        }

    }
}

fun pandemic(game: P2ndemic, gamestate: Gamestate, input: String): Gamestate {
    return when (input.split(" ")[0]) {
        "EPIDEMIC" -> game.epidemic(gamestate.copy(epidemics = gamestate.epidemics + 1))
        "REMOVE" -> gamestate.copy(discard = (gamestate.discard - input.split(" ")[1]))
        "LIST" -> {
            println("Discard = " + gamestate.discard.toString())
            println("Deck = " + gamestate.deck.toString())
            println("Epidemics = " + gamestate.epidemics)
            println("Cards Drawn = " + game.cardDrawNum(gamestate.epidemics))
            gamestate
        }
        else -> {
            game.draw(gamestate, input)
        }
    }
}
