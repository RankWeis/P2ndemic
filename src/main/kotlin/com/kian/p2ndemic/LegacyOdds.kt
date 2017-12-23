package com.kian.p2ndemic

fun oddsNextDraw(knownCards: List<String>, cardsDrawn: Int) : String {
    return when {
        knownCards.isEmpty() -> "Unknown card(s)"
        else ->  {
            val odds = findAllCardOdds(knownCards, cardsDrawn).toString()
            return odds + if (cardsDrawn > knownCards.size) " and ${cardsDrawn - knownCards.size} more" else ""
        }
    }
}


fun findAllCardOdds(knownCards: List<String>, cardsDrawn: Int) : Map<String, Double> {
    val occurences : Map<String, Int> = knownCards.groupingBy { it }.eachCount()
    return occurences.entries.fold(emptyMap(), {
        map, (key, value) -> map.plus( Pair(key, findOddsForCard(value, knownCards.size, cardsDrawn)))
    })

}

fun findOddsForCard(count: Int, totalCards: Int, cardsDrawn: Int) : Double {
    return 1 - ((0 until minOf(cardsDrawn, totalCards))
            .fold(1.0 , { acc, x -> acc * ((totalCards - x - count) / (totalCards - x).toDouble())}))
}
