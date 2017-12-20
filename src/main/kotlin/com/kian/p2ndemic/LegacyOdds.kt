package com.kian.p2ndemic

fun oddsNextDraw(knownCards: List<String>, cardsDrawn: Int) : String {
    return when {
        knownCards.isEmpty() -> "Unknown card(s)"
        else ->  {
            val odds = findAllCardOdds(knownCards, cardsDrawn).toString()
            return odds + if (cardsDrawn > knownCards.size) " and ${cardsDrawn - knownCards.size} unknown" else ""
        }

    }

}


fun findAllCardOdds(knownCards: List<String>, cardsDrawn: Int) : Map<String, Double> {
    val occurences : Map<String, Int> = knownCards.groupingBy { it }.eachCount()
    return occurences.entries.fold(emptyMap<String, Double>(), {
        map, entry -> map.plus( Pair(entry.key, findOddsForCard(entry.value, knownCards.size, cardsDrawn)))
    })

}

fun findOddsForCard(count: Int, totalCards: Int, cardsDrawn: Int) : Double {
    return 1 - ((0..minOf(cardsDrawn - 1, totalCards - 1)).fold(1.0 , { acc, x -> acc * ((totalCards - x - count) / (totalCards - x).toDouble())}))
}
