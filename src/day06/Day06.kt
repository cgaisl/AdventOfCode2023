package day06

import println
import readInput
import solveQuadraticEquation
import splitByWhitespaces
import kotlin.math.max

fun main() {
    fun part1(input: List<String>): Int {
        val (times, records) = input.parseTimesRecords()

        val results = times.mapIndexed { index, time ->
            List(time) { it }.map { calculateDistance(it.toLong(), time.toLong()) }.filter { it > records.get(index) }.size
        }

        return results.reduce { acc, i -> acc * i }
    }

    fun part2(input: List<String>): Int {
        val (time, record) = input.parseSingleRace()

        val solutions = solveQuadraticEquation((-1).toBigInteger(), time.toBigInteger(), (-record).toBigInteger())

        return (time - solutions.first.toLong() * 2).toInt() - 1

    }

    val testInput1 = readInput("day06/Day06_test1")
    val testInput2 = readInput("day06/Day06_test2")
    val input = readInput("day06/Day06")

    check(part1(testInput1) == 288)
    check(part2(testInput2) == 71503)
    part1(input).println()
    part2(input).println()
}

fun calculateDistance(hold: Long, duration: Long): Long {
    return max((duration - hold) * hold, 0)
}


fun List<String>.parseTimesRecords(): Pair<List<Int>, List<Int>> {
    return get(0).substringAfter("Time:").trim().splitByWhitespaces().map { it.toInt() } to
            get(1).substringAfter("Distance:").trim().splitByWhitespaces().map { it.toInt() }
}

fun List<String>.parseSingleRace(): Pair<Long, Long>{
    return get(0).substringAfter("Time:").filter { it != ' ' }.toLong() to
            get(1).substringAfter("Distance:").filter { it != ' ' }.toLong()
}
