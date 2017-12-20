package com.kian.p2ndemic


class P2ndemic {
    var discard: MutableList<String> = mutableListOf()
    var topDeck: MutableList<MutableList<String>> = mutableListOf()
    fun draw(card: String) {
        if (topDeck.isEmpty()) {
            topDeck.add(mutableListOf())
        }
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
    }

    fun epidemic() {
        topDeck.add(0, discard)
        discard = mutableListOf()
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

fun main(args: Array<String>): Unit {
    var game = P2ndemic()
    var epidemics = 0
    while (true) {
        println("Enter your card or epidemic:")
        var input: String = readLine()?.toUpperCase() ?: continue
        when (input) {
            "EPIDEMIC" -> {
                epidemics++; game.epidemic()
            }
            else -> {
                try {
                    game.draw(input)
                } catch (e: UnsupportedOperationException) {
                    println(e.message)
                    println("try again")
                }
            }
        }
        var cardsToDraw = game.cardDrawNum(epidemics)
        var cardsDrawn = 0;
        var currentList = 0;
        while (cardsDrawn < cardsToDraw) {
            if (currentList >= game.topDeck.size) {
                break
            }
            println(oddsNextDraw(game.topDeck.get(currentList), cardsToDraw))
            cardsDrawn += minOf(game.topDeck.get(currentList).size, cardsToDraw)
            currentList++
        }

    }
}
