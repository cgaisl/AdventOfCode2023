package day04

import println
import readInput
import kotlin.math.pow

fun main() {
    fun part1(input: List<String>): Int {
        val winnersAndNumbers = input.toWinnersAndNumbersSorted()
        var winnerSum = 0

        winnersAndNumbers.forEach { (winner, numbers) ->
            winnerSum += 2.0.pow(winner.intersect(numbers).size - 1).toInt()
        }

        return winnerSum
    }

    fun part2(input: List<String>): Int {
        val numberOfScratchcards = MutableList(input.size) { 1 }

        val winnersAndNumbers = input.toWinnersAndNumbersSorted()
        winnersAndNumbers.forEachIndexed { index, (winners, numbers) ->
            val points = winners.intersect(numbers).size
            repeat(points) {
                numberOfScratchcards[index + it + 1] += numberOfScratchcards[index]
            }
        }

        return numberOfScratchcards.reduce { acc, i -> acc + i }
    }

    val testInput1 = readInput("day04/Day04_test1")
    val testInput2 = readInput("day04/Day04_test2")
    val input = readInput("day04/Day04")

    check(part1(testInput1) == 13)
    check(part2(testInput2) == 30)
    part1(input).println()
    part2(input).println()
}

fun List<String>.toWinnersAndNumbersSorted(): List<Pair<Set<Int>, Set<Int>>> {
    val cards = map { it.substringAfter(": ") }

    return cards.map { line ->
        val winners = line.substringBefore(" |").trim().split("\\s+".toRegex()).map { it.toInt() }.toSet()
        val numbers = line.substringAfter("| ").trim().split("\\s+".toRegex()).map { it.toInt() }.toSet()
        winners to numbers
    }
}