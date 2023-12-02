package day02

import println
import readInput
fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput1 = readInput("day02/Day02_test1")
    val testInput2 = readInput("day02/Day02_test2")
    val input = readInput("Day02")

    check(part1(testInput1) == 0)
    check(part2(testInput2) == 0)
    part1(input).println()
    part2(input).println()
}
