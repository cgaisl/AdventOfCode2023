package day10

import P2
import get
import getOrNull
import println
import readInput

fun main() {
    fun part1(input: List<String>): Int {
        val map = input.parseInput()

        val tunnelTiles = map.getAllTunnelTilePositions()

        return (tunnelTiles.size) / 2
    }

    fun part2(input: List<String>): Int {
        val map = input.parseInput()

        val tunnelTiles = map.getAllTunnelTilePositions()

        var count = 0

        map.mapIndexed { y, line ->
            var inside = false

            line.mapIndexed { x, tile ->
                if (tile.crossing && tunnelTiles.contains(P2(x, y))) {
                    inside = !inside
                } else if (inside && !tunnelTiles.contains(P2(x, y))) {
                    count++
                }
            }
        }

        return count
    }

    val testInput1 = readInput("day10/Day10_test1")
    val testInput2 = readInput("day10/Day10_test2")
    val testInput3 = readInput("day10/Day10_test3")
    val testInput4 = readInput("day10/Day10_test4")
    val input = readInput("day10/Day10")

    check(part1(testInput1) == 8)
    check(part2(testInput2) == 4)
    check(part2(testInput3) == 8)
    check(part2(testInput4) == 10)
    part1(input).println()
    part2(input).println()
}

fun List<List<Tile>>.getAllTunnelTilePositions(): List<P2> {
    val startPos = P2(
        x = first { it.any { it.start } }
            .indexOfFirst { it.start },
        y = indexOfFirst { it.any { it.start } }
    )

    var prevPos = startPos
    var current =
        if (get(startPos).up) P2(startPos.x, startPos.y - 1)
        else if (get(startPos).down) P2(startPos.x, startPos.y + 1)
        else if (get(startPos).left) P2(startPos.x - 1, startPos.y)
        else P2(startPos.x + 1, startPos.y)

    val positions = mutableListOf(current)

    while (current != startPos) {
        val newPrev = current
        current = indicesOfNeighbors(current).first { it != prevPos }
        prevPos = newPrev
        positions.add(current)
    }

    return positions
}

data class Tile(
    val up: Boolean = false,
    val down: Boolean = false,
    val left: Boolean = false,
    val right: Boolean = false,
    val start: Boolean = false,
) {
    // we can test if a tile is inside the curve by checking if a ray cast from left to right crosses an odd number of crossing tiles. (Jordan Curve Theorem)
    val crossing: Boolean
        get() = (up && right) || (up && left) || (up && down)
}


fun List<String>.parseInput(): List<List<Tile>> {
    val tiles = mapIndexed { y, line ->
        line.mapIndexed { x, char ->
            val up = if (y > 0) true else false
            val down = if (y < size - 1) true else false
            val left = if (x > 0) true else false
            val right = if (x < line.length - 1) true else false

            when (char) {
                '.' -> Tile()
                '|' -> Tile(up = up, down = down)
                '-' -> Tile(left = left, right = right)
                'L' -> Tile(up = up, right = right)
                'J' -> Tile(left = left, up = up)
                '7' -> Tile(left = left, down = down)
                'F' -> Tile(right = right, down = down)
                'S' -> Tile(start = true)
                else -> throw Exception("Invalid char: $char")
            }
        }
    }

    return tiles.mapIndexed { y, line ->
        line.mapIndexed { x, tile ->
            if (tile.start) {
                val up = if (y == 0) false else null
                val down = if (y == size - 1) false else null
                val left = if (x == 0) false else null
                val right = if (x == line.size - 1) false else null

                tile.copy(
                    up = up ?: tiles.get(P2(x, y - 1)).down,
                    down = down ?: tiles.get(P2(x, y + 1)).up,
                    left = left ?: tiles.get(P2(x - 1, y)).right,
                    right = right ?: tiles.get(P2(x + 1, y)).left,
                )
            } else {
                tile
            }
        }
    }
}

fun List<List<Tile>>.indicesOfNeighbors(pos: P2): List<P2> {
    val tile = getOrNull(pos) ?: return emptyList()

    val neighbors = mutableListOf<P2>()
    if (tile.up) {
        neighbors.add(P2(pos.x, pos.y - 1))
    }
    if (tile.down) {
        neighbors.add(P2(pos.x, pos.y + 1))
    }

    if (tile.left) {
        neighbors.add(P2(pos.x - 1, pos.y))
    }

    if (tile.right) {
        neighbors.add(P2(pos.x + 1, pos.y))
    }

    return neighbors
}
