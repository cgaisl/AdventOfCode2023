package day16

import Direction
import Direction.*
import Pos
import columns
import println
import readInput
import rows
import toMutableCharGrid

fun main() {
    fun calculateEnergized(input: List<String>, initialRay: Ray): Int {
        val grid = input.toMutableCharGrid().map { it.map { Tile(it) } }.map { it.toMutableList() }.toMutableList()

        val rays = linkedSetOf(initialRay)

        var rayIndex = 0
        while (rayIndex < rays.size) {
            val ray = rays.toList()[rayIndex]
            while (true) {
                grid.getOrNull(ray.pos.y)?.getOrNull(ray.pos.x)?.energized = true
                ray.pos += ray.direction

                if (ray.pos.x < 0 || ray.pos.x >= grid.first().size || ray.pos.y < 0 || ray.pos.y >= grid.size) break

                when (grid[ray.pos.y][ray.pos.x].type) {
                    '|' -> when (ray.direction) {
                        UP, DOWN -> {}
                        LEFT, RIGHT -> {
                            rays += Ray(ray.pos, UP)
                            rays += Ray(ray.pos, DOWN)
                            break
                        }
                    }

                    '-' -> when (ray.direction) {
                        LEFT, RIGHT -> {}
                        UP, DOWN -> {
                            rays += Ray(ray.pos, LEFT)
                            rays += Ray(ray.pos, RIGHT)
                            break
                        }
                    }

                    '/' -> when (ray.direction) {
                        UP -> ray.direction = RIGHT
                        DOWN -> ray.direction = LEFT
                        LEFT -> ray.direction = DOWN
                        RIGHT -> ray.direction = UP
                    }

                    '\\' -> when (ray.direction) {
                        UP -> ray.direction = LEFT
                        DOWN -> ray.direction = RIGHT
                        LEFT -> ray.direction = UP
                        RIGHT -> ray.direction = DOWN
                    }
                }
            }
            rayIndex++
        }

        return grid.map { row ->
            row.count { it.energized }
        }.sum()
    }

    fun part1(input: List<String>): Int {
        return calculateEnergized(input, Ray(Pos(-1, 0), RIGHT))
    }

    fun part2(input: List<String>): Int {
        val grid = input.toMutableCharGrid()

        val edgePositions = grid.rows.first().mapIndexed { x, tile -> Pos(x, 0) }
            .map { Ray(pos = it.copy(it.x, it.y - 1), direction = DOWN) } +
                grid.rows.last().mapIndexed { x, tile -> Pos(x, grid.size - 1) }
                    .map { Ray(pos = it.copy(it.x, it.y + 1), direction = UP) } +
                grid.columns.first().mapIndexed { y, tile -> Pos(0, y) }
                    .map { Ray(pos = it.copy(it.x - 1, it.y), direction = RIGHT) } +
                grid.columns.last().mapIndexed { y, tile -> Pos(grid.first().size - 1, y) }
                    .map { Ray(pos = it.copy(it.x + 1, it.y), direction = LEFT) }

        return edgePositions.map { calculateEnergized(input, it) }.max()
    }

    val testInput1 = readInput("day16/Day16_test1")
    val testInput2 = readInput("day16/Day16_test2")
    val input = readInput("day16/Day16")

    check(part1(testInput1) == 46)
    check(part2(testInput2) == 51)
    part1(input).println()
    part2(input).println()
}

data class Tile(
    val type: Char,
    var energized: Boolean = false,
)

class Ray(
    var pos: Pos,
    var direction: Direction,
) {
    private val initial = pos to direction

    override fun hashCode(): Int {
        return initial.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return other is Ray && initial == other.initial
    }
}