package y23

import readInput

// https://adventofcode.com/2023/day/1
fun main() {
    fun extractDigitsAndConvertToInt(row: String): Int =
        "${row.first { it.isDigit() }}${row.last { it.isDigit() }}".toInt()

    // --- Part One ---
    fun calibrationValue(row: String): Int =
        extractDigitsAndConvertToInt(row)

    fun calculateCalibrationValuePart1(input: List<String>): Int =
        input.sumOf { calibrationValue(it) }

    // --- Part Two ---
    val numberWordSubstitutions = mapOf(
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
        numberWordSubstitutions.entries.fold(this) { result, (word, replacement) ->
            result.replace(word, replacement)
        }

    fun calculateCalibrationValuePart2(input: List<String>): Int =
        input.sumOf { row ->
            calibrationValue(
                row.replaceWords()
            )
        }

    val input = readInput("y23/Day01")

    // Part 1
    val testPart1 = readInput("y23/Day01_Part1_Test")
    check(calculateCalibrationValuePart1(testPart1) == 142)

    println("Part1 answer: ${calculateCalibrationValuePart1(input)}")

    // Part 2
    val testPart2 = readInput("y23/Day01_Part2_Test")
    check(calculateCalibrationValuePart2(testPart2) == 281)

    println("Part2 answer: ${calculateCalibrationValuePart2(input)}")
}
