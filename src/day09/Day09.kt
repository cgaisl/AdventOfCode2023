package day09

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int {
        return input.parseInput().map { it.nextInSequence() }.reduce(Int::plus)
    }

    fun part2(input: List<String>): Int {
        return input.parseInput().map { it.prevInSequence() }.reduce(Int::plus)
    }

    val testInput1 = readInput("day09/Day09_test1")
    val testInput2 = readInput("day09/Day09_test2")
    val input = readInput("day09/Day09")

    check(part1(testInput1) == 114)
    check(part2(testInput2) == 2)
    part1(input).println()
    part2(input).println()
}

fun List<String>.parseInput(): List<List<Int>> = map { it.split(" ").map { it.toInt() } }

fun List<Int>.diffTree(): List<List<Int>> {
    val difTree: MutableList<List<Int>> = mutableListOf(this)

    while (true) {
        val differences = difTree.last().zipWithNext { a, b -> b - a }
        if (differences.isEmpty()) break
        difTree.add(differences)
        if (differences.all { it == 0 }) break
    }

    return difTree
}

fun List<Int>.nextInSequence(): Int = diffTree().fold(0) { acc, list ->
    acc + (list.lastOrNull() ?: 0)
}

fun List<Int>.prevInSequence(): Int {
    return diffTree().reversed().fold(0) { acc, list ->
        (list.firstOrNull() ?: 0) - acc
    }
}
