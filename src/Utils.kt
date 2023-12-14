import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

fun LongRange.splitInto(n: Int): List<LongRange> {
    val chunkSize = (endInclusive - start + 1) / n
    val ranges = mutableListOf<LongRange>()

    var currentStart = start
    repeat(n) {
        val end = currentStart + chunkSize - 1
        ranges += currentStart..if (it == n - 1) endInclusive else end
        currentStart = end + 1
    }

    return ranges
}

fun String.splitByWhitespaces() = split("\\s+".toRegex())

// Calculate pair of solutions for quadratic equation ax^2 + bx + c = 0 using BigIntegers
fun solveQuadraticEquation(a: BigInteger, b: BigInteger, c: BigInteger): Pair<BigInteger, BigInteger> {
    val discriminant = b.pow(2) - 4.toBigInteger() * a * c

    val x1 = (-b + discriminant.sqrt()) / (2.toBigInteger() * a)
    val x2 = (-b - discriminant.sqrt()) / (2.toBigInteger() * a)

    return x1 to x2
}

fun List<Int>.lcm(): Long {
    fun gcd(a: Long, b: Long): Long {
        return if (b == 0L) {
            a
        } else {
            gcd(b, a % b)
        }
    }

    fun lcm(a: Long, b: Long): Long = (a * b) / gcd(a, b)

    var result = get(0).toLong()
    for (i in 1 until size) {
        result = lcm(result, get(i).toLong())
    }
    return result
}

data class Pos(val x: Int, val y: Int)

fun <T> List<List<T>>.get(pos: Pos): T = get(pos.y).get(pos.x)
fun <T> List<List<T>>.getOrNull(pos: Pos): T? = getOrNull(pos.y)?.getOrNull(pos.x)

fun <T> List<T>.allPossiblePairs(): Set<Pair<T, T>> {
    val pairs = mutableSetOf<Pair<T, T>>()

    forEachIndexed { index, t1 ->
        for (i in index + 1 until size) {
            pairs += t1 to get(i)
        }
    }

    return pairs
}

val <T> List<List<T>>.rows: List<List<T>>
    get() = this
val <T> List<List<T>>.columns: List<List<T>>
    get() = (0 until first().size).map { columnIndex -> map { it[columnIndex] } }

fun <T> List<List<T>>.transpose(): List<List<T>> = columns

typealias CharGrid = List<List<Char>>

typealias MutableCharGrid = MutableList<MutableList<Char>>

fun List<String>.toMutableCharGrid(): MutableCharGrid = map { it.toMutableList() }.toMutableList()

fun CharGrid.print() {
    println("Grid:")
    forEach { println(it.joinToString("")) }
}

fun MutableCharGrid.copy(): MutableCharGrid = map { it.toMutableList() }.toMutableList()