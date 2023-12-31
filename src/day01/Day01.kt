package day01

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int {
        return input
            .map { it.firstAndLastDigitAddedTogether() }
            .reduce(Int::plus)
    }

    fun part2(input: List<String>): Int {
        return input
//            .onEach { it.debugPrint() }
            .map {
                it
                    .replaceStringsWithDigits()
                    .firstAndLastDigitAddedTogether()
            }
            .reduce(Int::plus)
    }

    val testInput1 = readInput("day01/Day01_test1")
    val testInput2 = readInput("day01/Day01_test2")
    val input = readInput("day01/Day01")

    check(part1(testInput1) == 142)
    check(part2(testInput2) == 281)
    part1(input).println()
    part2(input).println()
}

fun String.firstAndLastDigitAddedTogether(): Int {
    return "${first { it.isDigit() }}${last { it.isDigit() }}".toInt()
}

fun String.replaceStringsWithDigits(): String {
    fun String.replaceStrings(): String {
        return this
            .replace("zero", "0o")
            .replace("one", "1e")
            .replace("two", "2o")
            .replace("three", "3e")
            .replace("four", "4")
            .replace("five", "5e")
            .replace("six", "6")
            .replace("seven", "7")
            .replace("eight", "8t")
            .replace("nine", "9e")
    }

    var currentString = ""

    forEach {
        currentString += it
        currentString = currentString.replaceStrings()
    }

    return currentString
}

@Suppress("unused")
fun String.debugPrint() {
    println("$this -> ${replaceStringsWithDigits()} : ${replaceStringsWithDigits().firstAndLastDigitAddedTogether()}")
}