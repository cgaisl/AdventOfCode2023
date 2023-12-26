package day18

import Direction
import P2
import getOrNull
import println
import readInput
import splitByWhitespaces
import kotlin.math.abs

@OptIn(ExperimentalStdlibApi::class)
fun main() {

    fun solve(instructions: List<Instruction>): Long {
        var boundaryPoints = 0
        var currentPoint = P2(0, 0)
        val area = instructions.fold(0L) { area, instruction ->
            val nextPoint = currentPoint.addInDirection(instruction.direction, instruction.distance)
            val newArea = area + (nextPoint.x - currentPoint.x) * currentPoint.y.toLong()

            boundaryPoints += instruction.distance
            currentPoint = nextPoint

            newArea
        }

        // Pick's theorem
        return abs(area) + boundaryPoints / 2 + 1
    }


    fun part1(input: List<String>): Int {
        val instructions = input.map {
            val parts = it.splitByWhitespaces()
            Instruction(
                direction = parts[0].toDirection(),
                distance = parts[1].toInt(),
            )
        }

        return solve(instructions).toInt()

        // This is the old solution, which is too slow for part 2

//        var currentPosition = P2(0, 0)
//
//        val positions = instructions.flatMap { instruction ->
//            List(instruction.distance) {
//                currentPosition += instruction.direction
//                currentPosition
//            }
//        }
//
//        val (minx, miny) = positions.minOf { it.x } to positions.minOf { it.y }
//
//        val normalizedPositions = positions.map { it + P2(abs(minx), abs(miny)) }
//
//        val (width, height) = normalizedPositions.maxOf { it.x } to normalizedPositions.maxOf { it.y }
//
//        val grid = Array(height + 1) { y ->
//            Array(width + 1) { x ->
//                when (P2(x, y)) {
//                    in normalizedPositions -> '#'
//                    else -> '.'
//                }
//            }
//        }.also {
//            it.print()
//        }
//
//        val filledGrid = grid
//            .mapIndexed { y, line ->
//                var insideHole = false
//                line.mapIndexed { x, char ->
//                    if (grid.isCrossingTile(P2(x, y))) insideHole = !insideHole
//                    if (insideHole) '#'
//                    else char
//                }
//            }
//            .also {
//                it.print()
//            }
//
//        return filledGrid.sumOf { row -> row.count { it == '#' } }
    }

    fun part2(input: List<String>): Long {
        val instructions = input.map {
            val parts = it.splitByWhitespaces()
            Instruction(
                direction = parts[2].dropLast(1).last().toDirection(),
                distance = parts[2].substring(2, 7).hexToInt(),
            )
        }

        return solve(instructions)
    }

    val testInput1 = readInput("day18/Day18_test1")
    val testInput2 = readInput("day18/Day18_test2")
    val input = readInput("day18/Day18")

    check(part1(testInput1) == 62)
    check(part2(testInput2) == 952408144115L)
    part1(input).println()
    part2(input).println()
}

data class Instruction(
    val direction: Direction,
    val distance: Int,
)

fun String.toDirection(): Direction = when (this) {
    "U" -> Direction.UP
    "D" -> Direction.DOWN
    "L" -> Direction.LEFT
    "R" -> Direction.RIGHT
    else -> throw Exception("Invalid direction: $this")
}

fun Char.toDirection(): Direction = when (this) {
    '3' -> Direction.UP
    '1' -> Direction.DOWN
    '2' -> Direction.LEFT
    '0' -> Direction.RIGHT
    else -> throw Exception("Invalid direction: $this")
}

// checks if tile is either a vertical, or a bottom-left or bottom-right corner of a horizontal
fun <T> Array<Array<T>>.isCrossingTile(p2: P2): Boolean =
    getOrNull(p2) == '#'
            && (
            ((getOrNull(p2 + Direction.LEFT) ?: '.') == '.' && (getOrNull(p2 + Direction.RIGHT) ?: '.') == '.')
                    ||
                    ((getOrNull(p2 + Direction.UP) ?: '.') == '#' && ((getOrNull(p2 + Direction.LEFT) ?: '.') == '.'))
                    ||
                    ((getOrNull(p2 + Direction.UP) ?: '.') == '#' && ((getOrNull(p2 + Direction.RIGHT) ?: '.') == '.'))
            )


