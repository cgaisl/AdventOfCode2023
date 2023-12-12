@file:Suppress("NAME_SHADOWING")

package day11

import Pos
import allPossiblePairs
import println
import readInput

fun main() {
    fun part1(input: List<String>): Int {
        val result = input.expandAndGetStarPos().allPossiblePairs().fold(0) { acc, (star1, star2) ->
            println(star1 to star2)
            acc + star1.distanceTo(star2)
        }

        return result
    }

    fun part2(input: List<String>, factor: Int): Long {
        val (stars, expansions) = input.getStarPosWithExpansionTable()

        return stars.allPossiblePairs().foldIndexed(0L) { index, acc, (star1, star2) ->
            acc + star1.expanded(expansions, factor).distanceTo(star2.expanded(expansions, factor))
        }
    }

    val testInput1 = readInput("day11/Day11_test1")
    val testInput2 = readInput("day11/Day11_test2")
    val input = readInput("day11/Day11")

    check(part1(testInput1) == 374)
    check(part2(testInput2, 100) == 8410L)
    part1(input).println()
    part2(input, 1000000).println()
}

fun Pos.distanceTo(other: Pos): Int {
    return Math.abs(x - other.x) + Math.abs(y - other.y)
}

data class ExpansionTable(
    val xExpansion: Map<Int, Int>,
    val yExpansion: Map<Int, Int>,
)

fun Pos.expanded(expansionTable: ExpansionTable, factor: Int): Pos {
    return copy(
        x = x + expansionTable.xExpansion[x]!! * (factor - 1),
        y = y + expansionTable.yExpansion[y]!! * (factor - 1)
    )
}

fun List<String>.getStarPosWithExpansionTable(): Pair<List<Pos>, ExpansionTable> {
    val universe = map { it.toCharArray() }

    val xExpansionPoints = mutableListOf<Int>()
    for (x in 0 until universe.first().size) {
        if (universe.map { it.get(x) }.all { it == '.' }) {
            xExpansionPoints += x
        }
    }

    val yExpansionPoints = mutableListOf<Int>()
    for (y in 0 until universe.size) {
        if (universe[y].all { it == '.' }) {
            yExpansionPoints += y
        }
    }

    val starPos = universe.flatMapIndexed { y, list ->
        list.toList().mapIndexedNotNull { x, element ->
            if (element == '#') Pos(x, y)
            else null
        }
    }

    return starPos to ExpansionTable(
        xExpansion = List(universe.first().size) { it }.associateWith { index -> xExpansionPoints.count { index > it } },
        yExpansion = List(universe.size) { it }.associateWith { index -> yExpansionPoints.count { index > it } },
    )
}

fun List<String>.expandAndGetStarPos(): List<Pos> {
    val universe = map {
        it.toCharArray().toMutableList()
    }.toMutableList()

    var x = 0
    while (x < universe.first().size) {
        if (universe.map { it.get(x) }.all { it == '.' }) {
            universe.forEachIndexed { index, pos ->
                pos.also { it.add(x, '.') }
            }
            x++
        }
        x++
    }

    var y = 0
    while (y < universe.size) {
        if (universe[y].all { it == '.' }) {
            universe.add(y, MutableList(universe[y].size) { '.' })
            y++
        }
        y++
    }

    return universe.flatMapIndexed { y, list ->
        list.mapIndexedNotNull { x, element ->
            if (element == '#') Pos(x, y)
            else null
        }
    }
}