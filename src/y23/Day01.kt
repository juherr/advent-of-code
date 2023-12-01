package y23

import readInput

// https://adventofcode.com/2023/day/1
fun main() {
    // --- Part One ---
    fun calibrationValue(row: String): Int =
        "${row.first { it.isDigit() }}${row.last { it.isDigit() }}".toInt()

    fun part1(input: List<String>): Int =
        input.sumOf { calibrationValue(it) }

    // --- Part Two ---
    val words = mapOf(
        "one" to "one1one",
        "two" to "two2two",
        "three" to "three3three",
        "four" to "four4four",
        "five" to "five5five",
        "six" to "six6six",
        "seven" to "seven7seven",
        "eight" to "eight8eight",
        "nine" to "nine9nine",
    )

    fun String.replaceWords(): String =
        words.entries.fold(this) { acc, (word, replacement) ->
            acc.replace(word, replacement)
        }

    fun part2(input: List<String>): Int =
        input.sumOf { row ->
            calibrationValue(
                row.replaceWords()
            )
        }

    val input = readInput("y23/Day01")

    val testPart1 = readInput("y23/Day01_Part1_Test")
    check(part1(testPart1) == 142)
    println("Part1 answer: ${part1(input)}")

    val testPart2 = readInput("y23/Day01_Part2_Test")
    check(part2(testPart2) == 281)
    println("Part2 answer: ${part2(input)}")
}
