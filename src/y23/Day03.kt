package y23

import readInput

// https://adventofcode.com/2023/day/3
fun main() {
    data class Number(val value: String, val coord: Pair<Int, Int>)
    data class Symbol(val value: Char, val coord: Pair<Int, Int>)

    fun Char.isSymbol() = !(this.isDigit() || this == '.')

    fun captureSymbols(schematic: List<String>) = schematic.flatMapIndexed { i, row ->
        row.mapIndexedNotNull { j, char ->
            if (char.isSymbol()) {
                Symbol(schematic[i][j], i to j)
            } else null
        }
    }

    fun captureNumbers(schematic: List<String>) =
        schematic.flatMapIndexed { index, line ->
            Regex("(\\d++)").findAll(line).map {
                Number(it.value, index to it.range.first)
            }.toList()
        }

    fun filterPart(numbers: List<Number>, symbols: List<Pair<Int, Int>>) =
        numbers.mapNotNull {
            val symbol = symbols.firstOrNull { symbol ->
                symbol.first in it.coord.first - 1..it.coord.first + 1
                        && symbol.second in it.coord.second - 1..it.coord.second + it.value.length
            }
            if (symbol != null) {
                it
            } else {
                null
            }
        }

    fun getPart1Result(schematic: List<String>): Int {
        val symbols = captureSymbols(schematic)
        val numbers = captureNumbers(schematic)
        return filterPart(numbers, symbols.map { it.coord }).sumOf { it.value.toInt() }
    }

    fun getPart2Result(schematic: List<String>): Int {
        val gears = captureSymbols(schematic).filter { it.value == '*' }
        val numbers = captureNumbers(schematic)
        return gears.sumOf { gear ->
            val filtered = numbers.filter { number ->
                gear.coord.first in number.coord.first - 1..number.coord.first + 1
                        && gear.coord.second in number.coord.second - 1..number.coord.second + number.value.length
            }
            if (filtered.size == 2) {
                filtered.map { it.value.toInt() }.reduce { acc, i -> acc * i }
            } else {
                0
            }
        }
    }

    val input = readInput("y23/Day03")
    val testPart = readInput("y23/Day03_Test")

    check(getPart1Result(testPart) == 4361)
    println("Part1 answer: ${getPart1Result(input)}")

    check(getPart2Result(testPart) == 467835)
    println("Part2 answer: ${getPart2Result(input)}")
}
