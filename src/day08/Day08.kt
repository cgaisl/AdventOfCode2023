package day08

import lcm
import println
import readInput

fun main() {
    fun part1(input: List<String>): Int {

        val (instructions, nodes) = input.parseInput()

        var currentNode = "AAA"
        var index = 0
        var count = 0

        while (true) {
            val instruction = instructions[index]

            when (instruction) {
                'L' -> {
                    currentNode = nodes.get(currentNode)!!.first
                }

                'R' -> {
                    currentNode = nodes.get(currentNode)!!.second
                }
            }

            index = (index + 1) % instructions.length
            count++

            if (currentNode == "ZZZ") {
                break
            }
        }

        return count
    }

    fun part2(input: List<String>): Long {
        val (instructions, nodes) = input.parseInput()

        val startNodes = nodes.filterKeys { it.endsWith('A') }

        val currentNodes = startNodes.keys.toMutableList()
        var instructionIndex = 0
        var count = 0
        val counts = MutableList(currentNodes.size) { 0 }

        while (true) {
            val instruction = instructions[instructionIndex]

            currentNodes.forEachIndexed { nodeIndex, node ->
                when (instruction) {
                    'L' -> {
                        currentNodes[nodeIndex] = nodes.get(node)!!.first
                    }

                    'R' -> {
                        currentNodes[nodeIndex] = nodes.get(node)!!.second
                    }
                }
            }

            instructionIndex = (instructionIndex + 1) % instructions.length
            count++

            currentNodes.forEachIndexed { nodeIndex, node ->
                if (node.endsWith('Z')) {
                    counts[nodeIndex] = count;
                }
            }

            if(counts.all { it > 0 }) {
                break
            }
        }

        return counts.lcm()
    }

    val testInput1 = readInput("day08/Day08_test1")
    val testInput2 = readInput("day08/Day08_test2")
    val input = readInput("day08/Day08")

    check(part1(testInput1) == 2)
    check(part2(testInput2) == 6L)
    part1(input).println()
    part2(input).println()
}


fun List<String>.parseInput(): Pair<String, Map<String, Pair<String, String>>> {
    val instructions = first()

    val nodes = drop(2).map {
        val name = it.substringBefore(" ")

        val left = it.substringAfter("(").substringBefore(",")
        val right = it.substringAfter(", ").substringBefore(")")

        Pair(name, Pair(left, right))
    }.associateBy({ it.first }, { it.second })

    return instructions to nodes
}