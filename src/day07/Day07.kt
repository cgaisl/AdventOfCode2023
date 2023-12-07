package day07

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int {

        val hands = input.parseHands()

        val result = hands
            .sorted()
            .foldIndexed(0) { index, acc, hand ->
                acc + (index + 1) * hand.bid
            }

        return result
    }

    fun part2(input: List<String>): Int {
        val hands = input.parseHands()

        val result = hands
            .sortedWith { a, b -> a.jCompareTo(b) }
            .foldIndexed(0) { index, acc, hand ->
                acc + (index + 1) * hand.bid
            }

        return result
    }

    val testInput1 = readInput("day07/Day07_test1")
    val testInput2 = readInput("day07/Day07_test2")
    val input = readInput("day07/Day07")

    check(part1(testInput1) == 6440)
    check(part2(testInput2) == 5905)
    part1(input).println()
    part2(input).println()
}

enum class Level {
    highCard,
    onePair,
    twoPairs,
    threeOfAKind,
    fullHouse,
    fourOfAKind,
    fiveOfAKind,
}

enum class Card {
    two,
    three,
    four,
    five,
    six,
    seven,
    eight,
    nine,
    ten,
    jack,
    queen,
    king,
    ace;

    companion object {
        fun fromChar(char: Char): Card {
            return when (char) {
                '2' -> two
                '3' -> three
                '4' -> four
                '5' -> five
                '6' -> six
                '7' -> seven
                '8' -> eight
                '9' -> nine
                'T' -> ten
                'J' -> jack
                'Q' -> queen
                'K' -> king
                'A' -> ace
                else -> throw IllegalArgumentException("Invalid card char: $char")
            }
        }
    }
}

fun Char.toCard(): Card = Card.fromChar(this)

data class Hand(
    val cards: String,
    val bid: Int
) : Comparable<Hand> {
    override operator fun compareTo(other: Hand): Int {
        if (level != other.level) {
            return level.compareTo(other.level)
        } else {
            cards.zip(other.cards)
                .map { (a, b) -> a.toCard() to b.toCard() }
                .forEach { (a, b) ->
                    if (a != b) {
                        return a.compareTo(b)
                    }
                }
            return 0
        }
    }

    fun jCompareTo(other: Hand): Int {
        if (jLevel != other.jLevel) {
            return jLevel.compareTo(other.jLevel)
        } else {
            cards.zip(other.cards)
                .map { (a, b) -> a.toCard() to b.toCard() }
                .forEach { (a, b) ->
                    if (a != b) {
                        if (a == Card.jack) return -1
                        if (b == Card.jack) return 1
                        return a.compareTo(b)
                    }
                }
            return 0
        }
    }

    val level: Level by lazy {
        val counts = cards.groupingBy { it }.eachCount().values.sorted()

        when (counts.size) {
            1 -> Level.fiveOfAKind
            2 -> if (counts[0] == 1) Level.fourOfAKind else Level.fullHouse
            3 -> if (counts[1] == 1) Level.threeOfAKind else Level.twoPairs
            4 -> Level.onePair
            else -> Level.highCard
        }
    }

    val jLevel: Level by lazy {
        if (cards == "JJJJJ") return@lazy Level.fiveOfAKind

        val bestCard = cards.filter { it != 'J' }.groupingBy { it }.eachCount().maxBy { it.value }.key
        val jCards = cards.replace('J', bestCard)
        val counts = jCards.groupingBy { it }.eachCount().values.sorted()

        when (counts.size) {
            1 -> Level.fiveOfAKind
            2 -> if (counts[0] == 1) Level.fourOfAKind else Level.fullHouse
            3 -> if (counts[1] == 1) Level.threeOfAKind else Level.twoPairs
            4 -> Level.onePair
            else -> Level.highCard
        }
    }
}

fun List<String>.parseHands(): List<Hand> {
    return map { line ->
        val (cards, bid) = line.split(" ")

        Hand(cards, bid.toInt())
    }
}