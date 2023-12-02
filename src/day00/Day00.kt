package day00

import println
import readInput
fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput1 = readInput("day00/Day00_test1")
    val testInput2 = readInput("day00/Day00_test2")
    val input = readInput("Day00")

    check(part1(testInput1) == 0)
    check(part2(testInput2) == 0)
    part1(input).println()
    part2(input).println()
}
