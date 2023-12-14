package day14

import MutableCharGrid
import copy
import println
import readInput
import toMutableCharGrid

fun main() {
    fun part1(input: List<String>): Int {
        val grid = input.toMutableCharGrid()

        for (x in 0 until grid.first().size) for (y in 1 until grid.size) {
            if (grid[y][x] != 'O') continue
            var rockAboveIndex = y - 1
            while (rockAboveIndex >= 0 && grid[rockAboveIndex][x] == '.') {
                grid[rockAboveIndex][x] = 'O'
                grid[rockAboveIndex + 1][x] = '.'
                rockAboveIndex--
            }
        }

        return grid.mapIndexed { y, rows ->
            rows.mapIndexed { _, c ->
                if (c == 'O') grid.size - y else 0
            }.sum()
        }.sum()
    }

    fun part2(input: List<String>): Int {
        val grid = input.toMutableCharGrid()

        val results = linkedSetOf(grid.copy())
        val cycleStart: Int
        val cycleSize: Int

        while (true) {
            grid.cycle()
            if (grid in results) {
                cycleStart = results.indexOf(grid)
                cycleSize = results.size - cycleStart
                break
            }
            results.add(grid.copy())
        }

        return results.toList()[cycleStart + (1000000000 - cycleStart) % cycleSize].mapIndexed { y, rows ->
            rows.mapIndexed { _, c ->
                if (c == 'O') grid.size - y else 0
            }.sum()
        }.sum()

    }

    val testInput1 = readInput("day14/Day14_test1")
    val testInput2 = readInput("day14/Day14_test2")
    val input = readInput("day14/Day14")

    check(part1(testInput1) == 136)
    check(part2(testInput2) == 64)
    part1(input).println()
    part2(input).println()
}


fun MutableCharGrid.cycle() {
    // north
    for (x in 0 until first().size) for (y in 1 until size) {
        if (this[y][x] != 'O') continue
        var rockAboveIndex = y - 1
        while (rockAboveIndex >= 0 && this[rockAboveIndex][x] == '.') {
            this[rockAboveIndex][x] = 'O'
            this[rockAboveIndex + 1][x] = '.'
            rockAboveIndex--
        }
    }

    // west
    for (y in 0 until size) for (x in 1 until first().size) {
        if (this[y][x] != 'O') continue
        var rockAboveIndex = x - 1
        while (rockAboveIndex >= 0 && this[y][rockAboveIndex] == '.') {
            this[y][rockAboveIndex] = 'O'
            this[y][rockAboveIndex + 1] = '.'
            rockAboveIndex--
        }
    }

    // south
    for (x in 0 until first().size) for (y in size - 2 downTo 0) {
        if (this[y][x] != 'O') continue
        var rockAboveIndex = y + 1
        while (rockAboveIndex < size && this[rockAboveIndex][x] == '.') {
            this[rockAboveIndex][x] = 'O'
            this[rockAboveIndex - 1][x] = '.'
            rockAboveIndex++
        }
    }

    //east
    for (y in 0 until size) for (x in first().size - 2 downTo 0) {
        if (this[y][x] != 'O') continue
        var rockAboveIndex = x + 1
        while (rockAboveIndex < first().size && this[y][rockAboveIndex] == '.') {
            this[y][rockAboveIndex] = 'O'
            this[y][rockAboveIndex - 1] = '.'
            rockAboveIndex++
        }
    }
}