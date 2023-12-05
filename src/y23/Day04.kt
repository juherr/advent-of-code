package y23

import kotlin.math.pow
import readInput

fun main() {
    data class Card(val id: Int, val winningNumbers: List<Int>, val numbers: List<Int>) {
        val matchingNumbers = numbers.filter { it in winningNumbers }
        val points = if (matchingNumbers.isEmpty()) 0 else 2.0.pow(matchingNumbers.count() - 1).toInt()
    }

    fun List<Card>.calculatePoints() = this.sumOf(Card::points)

    fun parseNumbersFromString(numbersString: String) =
        numbersString.split(" +".toRegex()).filter { it.isNotBlank() }.map { it.toInt() }

    fun List<String>.toCards(): List<Card> {
        val regex = "Card +(\\d+):((?: +\\d+)*) \\|((?: +\\d+)*)".toRegex()
        return this.map { cardString ->
            val match = regex.find(cardString)
            check(match != null) { "Invalid card string format: $cardString" }
            val (id, winningNumbersString, numbersString) = match.destructured
            val winningNumbers = parseNumbersFromString(winningNumbersString.trim())
            val numbers = parseNumbersFromString(numbersString)
            Card(id.toInt(), winningNumbers, numbers)
        }
    }

    fun List<Card>.calculateCardCount(): Int {
        val cardCounts = this.associate { it.id to 1 }.toMutableMap()
        this.forEach { card ->
            (card.id + 1..card.id + card.matchingNumbers.count()).forEach { id ->
                cardCounts[id] = cardCounts[id]!! + cardCounts[card.id]!!
            }
        }
        return cardCounts.values.sum()
    }

    val input = readInput("y23/Day04")
    val testInput = readInput("y23/Day04_Test")
    check(testInput.toCards().calculatePoints() == 13)

    println("Part1 answer: ${input.toCards().calculatePoints()}")

    check(testInput.toCards().calculateCardCount() == 30)
    println("Part2 answer: ${input.toCards().calculateCardCount()}")
}
