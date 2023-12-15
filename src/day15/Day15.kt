package day15

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int {
        return input.first().split(",")
            .map { hash(it) }
            .sum()
    }

    fun part2(input: List<String>): Int {
        val boxes = MutableList<MutableList<Lens>>(256) { mutableListOf() }

        input.first().split(",")
            .forEach {
                val label = it.takeWhile { it.isLetter() }
                val hash = hash(label)
                val sign = it.dropWhile { it.isLetter() }.first()
                val strength = it.dropWhile { !it.isDigit() }.toIntOrNull()
                val lens = Lens(label, strength ?: 0)

                when (sign) {
                    '=' -> {
                        boxes[hash].indexOfFirst { it.label == label }.let { index ->
                            if (index != -1) boxes[hash][index] = lens
                            else boxes[hash].add(lens)
                        }
                    }

                    '-' -> boxes[hash].removeAll { it.label == label }
                    else -> throw Exception("Unknown char")
                }
            }

        return boxes.mapIndexed { box, lenses ->
            lenses.mapIndexed { slot, lens ->
                lens.strength * (box + 1) * (slot + 1)
            }.sum()
        }.sum()
    }

    val testInput1 = readInput("day15/Day15_test1")
    val testInput2 = readInput("day15/Day15_test2")
    val input = readInput("day15/Day15")

    check(part1(testInput1) == 1320)
    check(part2(testInput2) == 145)
    part1(input).println()
    part2(input).println()
}

data class Lens(
    val label: String,
    val strength: Int,
)

fun hash(it: String): Int {
    var hash = 0

    it.forEach {
        hash += it.code
        hash *= 17
        hash %= 256
    }

    return hash
}