package y23

import readInput

// https://adventofcode.com/2023/day/3
fun main() {
    data class Number(val value: String, val coord: Pair<Int, Int>)

    fun captureSymbols(schematic: List<String>): List<Pair<Pair<Int, Int>, Char>> {
        val symbols = mutableListOf<Pair<Pair<Int, Int>, Char>>()
        for (i in schematic.indices) {
            for (j in schematic[i].indices) {
                if (!schematic[i][j].isDigit() && schematic[i][j] != '.') {
                    symbols.add(i to j to schematic[i][j])
                }
            }
        }
        return symbols
    }

    fun captureNumbers(schematic: List<String>): Map<Pair<Int, Int>, String> {
        val numbers = mutableMapOf<Pair<Int, Int>, String>()
        for (i in schematic.indices) {
            var str = ""
            var coord: Pair<Int, Int>? = null
            for (j in schematic[i].indices) {
                if (schematic[i][j].isDigit()) {
                    if (coord == null) {
                        check(str.isEmpty())
                        coord = i to j
                    }
                    str += schematic[i][j]
                } else {
                    if (str.isNotEmpty() && coord != null) {
                        numbers[coord] = str
                        str = ""
                        coord = null
                    }
                }
            }
            if (str.isNotEmpty() && coord != null) {
                numbers[coord] = str
            }
        }
        return numbers
    }

    fun filterPart(numbers: Map<Pair<Int, Int>, String>, symbols: List<Pair<Int, Int>>): List<Number> {
        val filtered = mutableListOf<Number>()
        for (number in numbers) {
            val symbol = symbols.firstOrNull { symbol ->
                symbol.first in number.key.first - 1..number.key.first + 1
                        && symbol.second in number.key.second - 1..number.key.second + number.value.length
            }
            if (symbol != null) {
                filtered.add(Number(number.value, number.key))
            }
        }
        return filtered
    }

    fun getPart1Result(schematic: List<String>): Int {
        val symbols = captureSymbols(schematic)
        val numbers = captureNumbers(schematic)
        return filterPart(numbers, symbols.map { it.first }).sumOf { it.value.toInt() }
    }

    fun getPart2Result(schematic: List<String>): Int {
        val gears = captureSymbols(schematic).filter { it.second == '*' }
        val numbers = captureNumbers(schematic)
        return gears.sumOf { gear ->
            val filtered = numbers.filter { number ->
                gear.first.first in number.key.first - 1..number.key.first + 1
                        && gear.first.second in number.key.second - 1..number.key.second + number.value.length
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
