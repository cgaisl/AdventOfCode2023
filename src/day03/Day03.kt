package day03

import println
import readInput
import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val lines = input.toNumbersAndSymbols()

        var numberSum = 0

        lines.forEachIndexed { index, line ->
            val relevantSymbols = lines.symbolsAt(index - 1) + line.symbols + lines.symbolsAt(index + 1)

            line.numbers.forEach { number ->
                if (number.isAdjacentToSymbols(relevantSymbols)) {
                    numberSum += number.value
                }
            }
        }

        return numberSum
    }

    fun part2(input: List<String>): Int {
        val lines = input.toNumbersAndSymbols()

        var gearRatioSum = 0

        lines.forEachIndexed { index, line ->
            val relevantNumbers = lines.numbersAt(index - 1) + line.numbers + lines.numbersAt(index + 1)

            line.symbols.filter { it.value == '*' }.forEach {
                it.isAdjacentToExactlyTwoNumbers(relevantNumbers)?.let { (number1, number2) ->
                    gearRatioSum += number1.value * number2.value
                }
            }
        }

        return gearRatioSum
    }

    val testInput1 = readInput("day03/Day03_test1")
    val testInput2 = readInput("day03/Day03_test2")
    val input = readInput("day03/Day03")

    check(part1(testInput1) == 4361)
    check(part2(testInput2) == 467835)
    part1(input).println()
    part2(input).println()
}

data class Line(
    val numbers: List<Number>,
    val symbols: List<Symbol>,
)

data class Position(
    val x: Int,
)

data class Number(
    val value: Int,
    val positionStart: Position,
    val positionEnd: Position,
)

data class Symbol(
    val value: Char,
    val position: Position,
)

fun List<Line>.symbolsAt(index: Int) = getOrNull(index)?.symbols ?: emptyList()
fun List<Line>.numbersAt(index: Int) = getOrNull(index)?.numbers ?: emptyList()

fun Symbol.isAdjacentToExactlyTwoNumbers(numbers: List<Number>): Pair<Number, Number>? {
    val adjacentNumbers = numbers.filter { it.isAdjacentToSymbols(listOf(this)) }
    return if (adjacentNumbers.size == 2) {
        adjacentNumbers[0] to adjacentNumbers[1]
    } else {
        null
    }
}

fun Number.isAdjacentToSymbols(symbols: List<Symbol>): Boolean {
    return symbols.fold(false) { acc, symbol ->
        acc || positionStart.isAdjacentTo(symbol.position) ||
                positionEnd.isAdjacentTo(symbol.position)
    }
}

fun Position.isAdjacentTo(position: Position): Boolean {
    return abs(x - position.x) <= 1
}

fun List<String>.toNumbersAndSymbols(): List<Line> {
    var currentNumber: String? = null
    val lines = mutableListOf<Line>()

    forEach { line ->
        val currentNumbers = mutableListOf<Number>()
        val currentSymbols = mutableListOf<Symbol>()

        line.forEachIndexed { x, char ->
            if (char.isDigit()) {
                currentNumber = currentNumber?.plus(char) ?: char.toString()
            } else {
                if (currentNumber != null) {
                    currentNumbers.add(
                        Number(
                            positionStart = Position(x - currentNumber!!.length),
                            positionEnd = Position(x - 1),
                            value = currentNumber!!.toInt(),
                        )
                    )
                    currentNumber = null
                }

                if (char != '.') {
                    currentSymbols.add(
                        Symbol(
                            position = Position(x),
                            value = char,
                        )
                    )
                }
            }
        }

        if (currentNumber != null) {
            currentNumbers.add(
                Number(
                    positionStart = Position(line.length - currentNumber!!.length),
                    positionEnd = Position(line.length - 1),
                    value = currentNumber!!.toInt(),
                )
            )
            currentNumber = null
        }

        lines.add(
            Line(
                numbers = currentNumbers,
                symbols = currentSymbols,
            )
        )
    }

    return lines
}