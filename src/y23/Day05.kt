package y23

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import readInput
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

fun main() {
    fun readSeedsPart1(seedsInput: String) =
        seedsInput.removePrefix("seeds: ").split(" ").map(String::toLong)

    fun readSeedsPart2(seedsInput: String): List<LongRange> {
        val values = seedsInput.removePrefix("seeds: ").split(" ")
        val temp = values.map(String::toLong)
        val result = mutableListOf<Pair<Long, Long>>()
        for (i in temp.indices step 2) {
            result.add(temp[i] to temp[i + 1])
        }
        return result.map { (start, length) -> (start..<start + length) }
    }

    fun List<String>.splitByEmptyLine(): List<List<String>> {
        val result = mutableListOf<List<String>>()
        var currentList = mutableListOf<String>()
        for (line in this) {
            if (line.isBlank()) {
                result.add(currentList)
                currentList = mutableListOf()
            } else {
                currentList.add(line)
            }
        }
        result.add(currentList)
        return result
    }

    data class Rule(val source: Long, val destination: Long, val length: Long)
    data class SeedMap(val from: String, val to: String, val rules: List<Rule>) {
        fun getDestination(source: Long) =
            rules.firstOrNull { it.destination <= source && source < it.destination + it.length }?.let {
                it.source + source - it.destination
            } ?: source
    }

    fun SeedMap.getLocationValue(value: Long, maps: Map<String, SeedMap>): Long {
        val dest = this.getDestination(value)
        return if (this.to == "location") {
            dest
        } else {
            maps[this.to]?.getLocationValue(dest, maps) ?: dest
        }
    }

    fun List<SeedMap>.getLocation(seed: Long): Long {
        val maps = this.groupBy { it.from }.mapValues { it.value.single() }
        return maps["seed"]?.getLocationValue(seed, maps) ?: throw IllegalStateException("No seed map found")
    }

    fun List<String>.mapToMap(): SeedMap {
        val (from, to) = this[0].removeSuffix(" map:").split("-to-")
        val lines = this.drop(1).map { it.split(" ").map(String::toLong) }
        return SeedMap(from, to, lines.map {
            check(it.size == 3) { "Invalid map line: $it" }
            Rule(it[0], it[1], it[2])
        })
    }

    fun part1(input: List<String>): Long {
        val seeds = readSeedsPart1(input[0])
        check(input[1].isBlank())
        val map = input.drop(2).splitByEmptyLine().map { it.mapToMap() }
        return seeds.minOfOrNull { map.getLocation(it) } ?: throw IllegalStateException("No seed found")
    }

    suspend fun getMin(range: LongRange, map: List<SeedMap>) = suspendCoroutine { continuation ->
        val result = range.minOfOrNull { map.getLocation(it) } ?: throw IllegalStateException("No seed found")
        continuation.resume(result)
    }

    fun part2(input: List<String>): Long {
        check(input[1].isBlank())
        val map = input.drop(2).splitByEmptyLine().map { it.mapToMap() }
        return runBlocking(Dispatchers.Default) {
            readSeedsPart2(input[0]).map { async { getMin(it, map) } }.awaitAll().min()
        }
    }

    val input = readInput("y23/Day05")
    val testInput = readInput("y23/Day05_Test")

    check(part1(testInput) == 35L)
    println("Part1 answer: ${part1(input)}")

    check(part2(testInput) == 46L)
    println("Part2 answer: ${part2(input)}")
}
