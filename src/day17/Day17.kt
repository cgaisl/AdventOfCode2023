package day17

import Direction
import Heap
import P2
import getOrNull
import println
import readInput

fun main() {
    fun part1(input: List<String>): Int {
        val grid = input.mapIndexed { y, s ->
            s.mapIndexed { x, c ->
                Node(P2(x, y), c.digitToInt())
            }
        }.map { it.toMutableList() }.toMutableList()


        val visited = mutableSetOf<Node>()
        val toVisit = Heap<Node, Int>()

        toVisit.putBetter(grid[0][0], 0)

        while (true) {
            val (current, currentDistance) = toVisit.removeMin()
            visited += current

            if (current.pos == P2(grid.first().size - 1, grid.size - 1)) return currentDistance

            grid.possibleNextNodes(current).forEach {
                val moveDirection = Direction.entries.find { dir -> current.pos + dir == it.pos }!!
                val newDirectionCount =
                    if (current.directionCount != null && current.directionCount.first == moveDirection) current.directionCount.second + 1 else 1

                if (newDirectionCount > 3) return@forEach

                val newNode = it.copy(directionCount = moveDirection to newDirectionCount)

                if (newNode !in visited)
                    toVisit.putBetter(newNode, currentDistance + it.weight)
            }
        }

        throw Exception("No path found")
    }

    fun part2(input: List<String>): Int {
        val grid = input.mapIndexed { y, s ->
            s.mapIndexed { x, c ->
                Node(P2(x, y), c.digitToInt())
            }
        }.map { it.toMutableList() }.toMutableList()


        val visited = mutableSetOf<Node>()
        val toVisit = Heap<Node, Int>()

        toVisit.putBetter(grid[0][0], 0)

        while (true) {
            val (current, currentDistance) = toVisit.removeMin()
            visited += current

            if (current.pos == P2(grid.first().size - 1, grid.size - 1)) return currentDistance

            grid.possibleNextNodes(current).forEach {
                val moveDirection = Direction.entries.find { dir -> current.pos + dir == it.pos }!!
                val newDirectionCount =
                    if (current.directionCount != null && current.directionCount.first == moveDirection) current.directionCount.second + 1 else 1


                if (newDirectionCount > 10) return@forEach
                if (newDirectionCount == 1 && (current.directionCount?.second ?: 4) < 4) return@forEach

                val newNode = it.copy(directionCount = moveDirection to newDirectionCount)

                if (newNode !in visited)
                    toVisit.putBetter(newNode, currentDistance + it.weight)
            }
        }

        throw Exception("No path found")
    }

    val testInput1 = readInput("day17/Day17_test1")
    val testInput2 = readInput("day17/Day17_test2")
    val input = readInput("day17/Day17")

    // part 2 not working

    check(part1(testInput1) == 102)
//    check(part2(testInput2) == 71)
    part1(input).println()
//    part2(input).println()
}

fun List<List<Node>>.possibleNextNodes(node: Node): List<Node> {
    val pos = node.pos
    val directions = when (node.directionCount?.first) {
        Direction.UP -> listOf(P2(1, 0), P2(-1, 0), P2(0, -1))
        Direction.DOWN -> listOf(P2(1, 0), P2(-1, 0), P2(0, 1))
        Direction.LEFT -> listOf(P2(-1, 0), P2(0, 1), P2(0, -1))
        Direction.RIGHT -> listOf(P2(1, 0), P2(0, 1), P2(0, -1))
        null -> listOf(P2(1, 0), P2(0, 1))
    }
    return directions.mapNotNull { this.getOrNull(pos + it) }
}

data class Node(
    val pos: P2,
    val weight: Int,
    val directionCount: Pair<Direction, Int>? = null,
)
