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
