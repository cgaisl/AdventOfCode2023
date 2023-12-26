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

data class P2(val x: Int, val y: Int) {
    operator fun plus(direction: Direction): P2 = when (direction) {
        Direction.UP -> P2(x, y - 1)
        Direction.DOWN -> P2(x, y + 1)
        Direction.LEFT -> P2(x - 1, y)
        Direction.RIGHT -> P2(x + 1, y)
    }

    fun addInDirection(direction: Direction, distance: Int): P2 = when (direction) {
        Direction.UP -> P2(x, y - distance)
        Direction.DOWN -> P2(x, y + distance)
        Direction.LEFT -> P2(x - distance, y)
        Direction.RIGHT -> P2(x + distance, y)
    }

    operator fun plus(other: P2): P2 = P2(x + other.x, y + other.y)
}

operator fun <T> List<List<T>>.get(pos: P2): T = get(pos.y).get(pos.x)
fun <T> List<List<T>>.getOrNull(pos: P2): T? = getOrNull(pos.y)?.getOrNull(pos.x)
fun <T> Array<Array<T>>.getOrNull(pos: P2): T? = getOrNull(pos.y)?.getOrNull(pos.x)
operator fun <T> MutableList<MutableList<T>>.set(pos: P2, value: T) {
    this[pos.y][pos.x] = value
}

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

fun <T> MutableList<MutableList<T>>.rotateClockwise() {
    val rotated = columns.map { it.reversed() }
    clear()
    addAll(rotated.toMutableList().map { it.toMutableList() })
}

typealias CharGrid = List<List<Char>>

typealias MutableCharGrid = MutableList<MutableList<Char>>

fun List<String>.toMutableCharGrid(): MutableCharGrid = map { it.toMutableList() }.toMutableList()

fun CharGrid.print() {
    println("Grid:")
    forEach { println(it.joinToString("")) }
}

fun <T> Array<Array<T>>.print() {
    println("Grid:")
    forEach { println(it.joinToString("")) }
}

fun <T> MutableList<MutableList<T>>.copy() = map { it.toMutableList() }.toMutableList()


enum class Direction {
    UP, DOWN, LEFT, RIGHT
}


class Heap<K, V : Comparable<V>> {
    private data class Data<V>(var v: V, var i: Int)

    private var size = 0
    private var h = arrayOfNulls<Any>(1024)
    private val d = HashMap<K, Data<V>>()

    fun isEmpty() = size == 0

    fun removeMin(): Pair<K, V> {
        val resK = h[0] as K
        val resV = d[resK]!!.v
        size--
        var i = 0
        val ik = h[size] as K
        val id = d[ik]!!
        h[0] = ik
        id.i = 0
        h[size] = null
        while (true) {
            var t = 2 * i + 1
            var j = i
            var jd = id
            if (t >= size) break
            var td = d[h[t]]!!
            if (td.v < jd.v) {
                j = t
                jd = td
            }
            t++
            if (t < size) {
                td = d[h[t]]!!
                if (td.v < jd.v) {
                    j = t
                    jd = td
                }
            }
            if (j == i) break
            val jk = h[j] as K
            h[i] = jk
            jd.i = i
            h[j] = ik
            id.i = j
            i = j
        }
        return resK to resV
    }

    fun putBetter(k: K, v: V): Boolean {
        val kd0 = d[k]
        var i: Int
        val id: Data<V>
        if (kd0 == null) {
            i = size++
            id = Data(v, i)
            if (i >= h.size) h = h.copyOf(h.size * 2)
            h[i] = k
            d[k] = id
        } else {
            if (kd0.v <= v) return false
            i = kd0.i
            id = kd0
            id.v = v
        }
        while (i > 0) {
            val j = (i - 1) / 2
            val jd = d[h[j]]!!
            if (jd.v <= id.v) break
            val jk = h[j] as K
            h[i] = jk
            jd.i = i
            h[j] = k
            id.i = j
            i = j
        }
        return true
    }
}
