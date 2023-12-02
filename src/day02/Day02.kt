package day02

import println
import readInput
import kotlin.math.max

fun main() {
    fun part1(input: List<String>): Int {

        var possibleGamesIdAccumulator = 0

        val red = 12
        val green = 13
        val blue = 14

        input.forEachIndexed { index, gameLine ->
            val game = Game.fromString(gameLine)
            if (game.isPossible(red, green, blue)) {
                possibleGamesIdAccumulator += index + 1
            }
        }

        return possibleGamesIdAccumulator
    }

    fun part2(input: List<String>): Int {
        return input.fold(0) { acc, gameLine ->
            val game = Game.fromString(gameLine)
            acc + game.minimumSetOfCubes().power()
        }
    }

    val testInput1 = readInput("day02/Day02_test1")
    val testInput2 = readInput("day02/Day02_test2")
    val input = readInput("day02/Day02")

    check(part1(testInput1) == 8)
    check(part2(testInput2) == 2286)
    part1(input).println()
    part2(input).println()
}

data class Cubes(
    val red: Int,
    val green: Int,
    val blue: Int
) {
    fun power(): Int {
        return red * green * blue
    }
}

data class Game(val turns: List<Cubes>) {

    companion object {
        fun fromString(input: String): Game {
            val turns = input
                .split(" ")
                .drop(2) // remove Game 1:
                .joinToString(" ")
                .split(";")
                .map {
                    val red = it.getCubes("red")
                    val green = it.getCubes("green")
                    val blue = it.getCubes("blue")
                    Cubes(red, green, blue)
                }
            return Game(turns)
        }
    }

    fun isPossible(red: Int, green: Int, blue: Int): Boolean {
        return turns.all { turn ->
            turn.red <= red && turn.green <= green && turn.blue <= blue
        }
    }

    fun minimumSetOfCubes(): Cubes {
        return turns.reduce { acc, cubes ->
            Cubes(
                red = max(acc.red, cubes.red),
                green = max(acc.green, cubes.green),
                blue = max(acc.blue, cubes.blue)
            )
        }
    }
}

fun String.getCubes(color: String): Int =
    wordBefore(color)?.toInt() ?: 0


fun String.wordBefore(word: String): String? {
    if (!contains(word)) return null
    return substringBefore(word).dropLast(1).split(" ").last()
}
