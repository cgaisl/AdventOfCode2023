package day13

import columns
import println
import readInput
import rows

typealias CharGrid = List<List<Char>>

fun main() {
    fun part1(input: List<String>): Int {

        val patterns = input.parse()

        val results = patterns.map { grid ->
            search(grid)
        }

        return results.map { it.first().value }.reduce(Int::plus)
    }

    fun part2(input: List<String>): Int {
        val patterns = input.parse()

        val results = patterns.map { grid ->
            val baseline = search(grid)
            var result = 0

            search@ for (y in 0 until grid.size) for (x in 0 until grid.first().size) {
                val newGrid = grid.toMutableList().map { it.toMutableList() }.apply {
                    this[y][x] = when (grid[y][x]) {
                        '.' -> '#'
                        '#' -> '.'
                        else -> error("Unknown char")
                    }
                }

                val newResult = search(newGrid) - baseline

                if (newResult.isNotEmpty()) {
                    result = newResult.first().value
                    break@search
                }
            }

            result
        }

        return results.reduce(Int::plus)
    }

    val testInput1 = readInput("day13/Day13_test1")
    val testInput2 = readInput("day13/Day13_test2")
    val input = readInput("day13/Day13")

    check(part1(testInput1) == 405)
    check(part2(testInput2) == 400)
    part1(input).println()
    part2(input).println()
}

sealed class LineDirection(val index: Int) {
    class Vertical(value: Int) : LineDirection(value)
    class Horizontal(value: Int) : LineDirection(value)

    val value: Int
        get() = when (this) {
            is Vertical -> index
            is Horizontal -> index * 100
        }

    override fun equals(other: Any?): Boolean {
        return  other is LineDirection && this.value == other.value
    }

    override fun hashCode(): Int {
        return index
    }
}


fun search(grid: CharGrid): Set<LineDirection> {
    var result: Set<LineDirection> = emptySet()

    grid.columns.forEachIndexed { index, column ->
        if (index == grid.first().size - 1) return@forEachIndexed
        var symmetrical = true
        var a = index
        var b = index + 1

        while (a >= 0 && b < grid.first().size) {
            if (grid.columns[a--] != grid.columns[b++]) {
                symmetrical = false
                break
            }
        }

        if (symmetrical) {
            result += LineDirection.Vertical(index +1)
        }
    }

    grid.rows.forEachIndexed { index, row ->
        if (index == grid.size - 1) return@forEachIndexed
        var symmetrical = true
        var a = index
        var b = index + 1

        while (a >= 0 && b < grid.size) {
            if (grid.rows[a--] != grid.rows[b++]) {
                symmetrical = false
                break
            }
        }

        if (symmetrical) {
            result += LineDirection.Horizontal(index + 1)
        }
    }

    return result
}

fun List<String>.parse(): List<CharGrid> {
    var counter = 0
    return groupBy { if (it.isEmpty()) counter++ else counter }
        .map { it.value.filter { it.isNotEmpty() } }
        .map { it.map { it.toList() } }
}
