package y23

import readInput

fun main() {
    data class Card(val id: Int, val winningNumbers: List<Int>, val numbers: List<Int>) {
        val matchingNumbers = numbers.filter { it in winningNumbers }
        fun getPoints(): Int {
            val matches = matchingNumbers.count()
            return if (matches > 0) 1.shl(matches - 1) else 0
        }
    }

    fun calculatePoints(cards: List<Card>) = cards.sumOf(Card::getPoints)

    fun parseCards(cardStrings: List<String>): List<Card> {
        val regex = "Card +(\\d+):((?: +\\d+)*) \\|((?: +\\d+)*)".toRegex()
        return cardStrings.map { cardString ->
            val match = regex.find(cardString)
            check(match != null) { "Invalid card string format: $cardString" }
            val (id, winningNumbersString, numbersString) = match.destructured
            val winningNumbers =
                winningNumbersString.trim().split(" +".toRegex()).filter { it.isNotBlank() }.map { it.toInt() }
            val numbers = numbersString.split(" +".toRegex()).filter { it.isNotBlank() }.map { it.toInt() }
            Card(id.toInt(), winningNumbers, numbers)
        }
    }

    fun calculateCardCount(cards: List<Card>): Int {
        val cardCounts = cards.associate { it.id to 1 }.toMutableMap()
        cards.forEach { card ->
            (card.id + 1..card.id + card.matchingNumbers.count()).forEach { id ->
                cardCounts[id] = cardCounts[id]!! + cardCounts[card.id]!!
            }
        }
        return cardCounts.values.sum()
    }

    val input = readInput("y23/Day04")
    val testInput = readInput("y23/Day04_Test")
    check(calculatePoints(parseCards(testInput)) == 13)

    println("Part1 answer: ${calculatePoints(parseCards(input))}")

    check(calculateCardCount(parseCards(testInput)) == 30)
    println("Part2 answer: ${calculateCardCount(parseCards(input))}")
}
