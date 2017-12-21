package com.kian.p2ndemic


typealias Card = String
typealias Cards = MutableList<Card>
typealias Gamestate = Pair<MutableList<Cards>, Cards>

class P2ndemic {
    fun draw(g: Gamestate, card: String) : Gamestate {
        val (topDeck, discard) = g
        val firstDeck = topDeck[0]
        if (!firstDeck.isEmpty()) {
            if (!firstDeck.contains(card)) {
                throw UnsupportedOperationException("That should not be possible!")
            }
            firstDeck.remove(card)
            if (firstDeck.isEmpty()) {
                topDeck.removeAt(0)
            }
        }
        discard.add(card)
        return Pair(topDeck, discard)
    }

    fun epidemic(g: Gamestate) : Gamestate {
        val (topDeck, discard) = g
        topDeck.add(0, discard)
        return Pair(topDeck, mutableListOf())
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
    var epidemics = 0
    var discard: MutableList<String> = mutableListOf()
    var topDeck: MutableList<MutableList<String>> = mutableListOf()
    topDeck.add(mutableListOf())
    while (true) {
        try {
            println("Enter your card or epidemic:")
            val input: String = readLine()?.toUpperCase() ?: continue
            when (input.split(" ")[0]) {
                "EPIDEMIC" -> {
                    epidemics++;
                    val gamestate = game.epidemic(Gamestate(topDeck, discard))
                    topDeck = gamestate.first
                    discard = gamestate.second
                }
                "REMOVE" -> discard.remove(input.split(" ")[1])
                "LIST" -> {
                    println("Discard = " + discard.toString())
                    println("Deck = " + topDeck.toString())
                    println("Epidemics = " + epidemics)
                    println("Cards Drawn = " + game.cardDrawNum(epidemics))

                }
                else -> {
                    val gamestate = game.draw(Gamestate(topDeck, discard), input)
                    topDeck = gamestate.first
                    discard = gamestate.second
                }
            }
        } catch (e: Exception) {
            println(e.message)
            println("try again")
        }

        val cardsToDraw = game.cardDrawNum(epidemics)
        var cardsDrawn = 0;
        var currentList = 0;
        while (cardsDrawn < cardsToDraw) {
            if (currentList >= topDeck.size) {
                break
            }
            println(oddsNextDraw(topDeck.get(currentList), (cardsToDraw - cardsDrawn)))
            cardsDrawn += minOf(topDeck.get(currentList).size, cardsToDraw)
            currentList++
        }

    }
}
