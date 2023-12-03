package y23

import readInput
import kotlin.math.max

// https://adventofcode.com/2023/day/2
fun main() {
    data class CubeSet(val red: Int, val green: Int, val blue: Int) {
        fun playableGames(gameData: Map<Int, List<CubeSet>>): List<Int> {
            return gameData.filter { (_, cubeSets) ->
                cubeSets.all { cubeSet ->
                    cubeSet.red <= this.red && cubeSet.green <= this.green && cubeSet.blue <= this.blue
                }
            }.keys.toList()
        }

        val power: Int = red * green * blue
    }

    fun String.toCubeSet(): CubeSet {
        val cubes = this.split(", ").associate {
            val (count, color) = it.split(" ")
            color to count.toInt()
        }
        return CubeSet(
            red = cubes["red"] ?: 0, green = cubes["green"] ?: 0, blue = cubes["blue"] ?: 0
        )
    }

    fun parseGameData(input: List<String>): Map<Int, List<CubeSet>> {
        return input.associate { row ->
            val (gameDescriptor, cubeSets) = row.split(": ")
            val cubeSetsList = cubeSets.split("; ").map { cubeSet ->
                cubeSet.toCubeSet()
            }
            val (_, gameId) = gameDescriptor.split(" ")
            gameId.toInt() to cubeSetsList
        }
    }

    fun getPart1Result(gameData: Map<Int, List<CubeSet>>): Int {
        val bag = CubeSet(red = 12, green = 13, blue = 14)
        val possibleGames = bag.playableGames(gameData)
        return possibleGames.sum()
    }

    val input = readInput("y23/Day02")
    val gameData = parseGameData(input)

    val testPart = readInput("y23/Day02_Test")
    val gameDataTest = parseGameData(testPart)
    check(getPart1Result(gameDataTest) == 8)

    println("Part1 answer: ${getPart1Result(gameData)}")

    fun getMinimumCubesForGame(gameData: List<CubeSet>) =
        gameData.fold(CubeSet(0, 0, 0)) { accumulator, cubeSet ->
            CubeSet(
                max(accumulator.red, cubeSet.red),
                max(accumulator.green, cubeSet.green),
                max(accumulator.blue, cubeSet.blue)
            )
        }

    fun getPart2Result(gameData: Map<Int, List<CubeSet>>) =
        gameData.values.map(::getMinimumCubesForGame).sumOf { it.power }

    check(getPart2Result(gameDataTest) == 2286)

    println("Part2 answer: ${getPart2Result(gameData)}")
}
