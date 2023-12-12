package day12

import println
import readInput

fun main() {
    fun part1(input: List<String>): Long {
        return input
            .parsed()
            .map { (springs, groupSizes) ->
                solutions(springs, groupSizes)
            }
            .reduce(Long::plus)
    }

    fun part2(input: List<String>): Long {
        return input
            .parsed()
            .map { (springs, groupSizes) ->
                List(5) { springs + '?' }.flatten().dropLast(1) to
                        List(5) { groupSizes }.flatten()
            }
            .map { (springs, groupSizes) ->
                solutions(springs, groupSizes)
            }
            .reduce(Long::plus)
    }

    val testInput1 = readInput("day12/Day12_test1")
    val testInput2 = readInput("day12/Day12_test2")
    val input = readInput("day12/Day12")

    check(part1(testInput1) == 21L)
    check(part2(testInput2) == 525152L)
    part1(input).println()
    part2(input).println()
}

val memory = mutableMapOf<Pair<List<Char>, List<Int>>, Long>()

fun solutions(
    springs: List<Char>,
    groups: List<Int>,
): Long {
    if (springs.isEmpty() && groups.isEmpty()) return 1
    if (springs.isEmpty()) return 0

    return when (springs.first()) {
        '.' -> solutions(springs.drop(1), groups)
        '#' -> damagedSpringSolutions(springs, groups).also { memory[springs to groups] = it }
        '?' -> solutions(springs.drop(1), groups) +
                damagedSpringSolutions(springs, groups).also { memory[springs to groups] = it }

        else -> throw Exception("something went terriby wrong")
    }
}

fun damagedSpringSolutions(
    springs: List<Char>,
    groups: List<Int>,
): Long {
    memory[springs to groups]?.let { return it }

    if (groups.isEmpty()) return 0

    val groupSize = groups.first()

    if (groupSize > springs.size) return 0

    for (i in 0 until groupSize) {
        if (springs[i] == '.') return 0
    }

    if (springs.size == groupSize) {
        if (groups.size == 1) return 1
        return 0
    }

    if (springs[groupSize] == '#') return 0

    return solutions(springs.drop(groupSize + 1), groups.drop(1))
}


fun List<String>.parsed(): List<Pair<List<Char>, List<Int>>> = map {
    it.substringBefore(" ").toCharArray().toList() to it.substringAfter(" ").split(",").map { it.toInt() }
}