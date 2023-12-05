@file:OptIn(DelicateCoroutinesApi::class)

package day05

import kotlinx.coroutines.*
import println
import readInput
import splitInto

fun main() {
    fun part1(input: List<String>): Int {
        val seeds = input.seeds
        val getLocation = getGetLocationFunction(input)

        var lowestLocation = Long.MAX_VALUE

        seeds.forEach {
            val location = getLocation(it)

            if (location < lowestLocation) {
                lowestLocation = location
            }
        }

        return lowestLocation.toInt()
    }

    fun part2(input: List<String>): Int = runBlocking {
        val seedRanges = input.seedsPart2
        val getLocation = getGetLocationFunction(input)

        val jobs = mutableListOf<Job>()
        var lowestLocation = Long.MAX_VALUE

        seedRanges
            .flatMap { it.splitInto(10) }
            .forEach { range ->
                jobs.add(
                    GlobalScope.launch {
                        range.forEach {
                            val location = getLocation(it)

                            if (location < lowestLocation) {
                                lowestLocation = location
                            }
                        }
                    }
                )
            }

        jobs.forEach { it.join() }  // Takes a hot minute, ~38s on M2 Max

        return@runBlocking lowestLocation.toInt()
    }

    val testInput1 = readInput("day05/Day05_test1")
    val testInput2 = readInput("day05/Day05_test2")
    val input = readInput("day05/Day05")

    check(part1(testInput1) == 35)
    check(part2(testInput2) == 46)
    part1(input).println()
    part2(input).println()

//    measureTime { part2(input).println() }.println()
}

val List<String>.seeds: List<Long>
    get() = this.first().substringAfter("seeds: ").split(" ").map { it.toLong() }

val List<String>.seedsPart2: List<LongRange>
    get() =
        first()
            .substringAfter("seeds: ")
            .split(" ")
            .map { it.toLong() }
            .windowed(2, 2) {
                it.first() to it.last()
            }
            .map { (start, end) ->
                LongRange(start, start + end)
            }

fun getGetLocationFunction(input: List<String>): (Long) -> Long {
    val seedToSoilMap = input.extractTransformationFor("seed-to-soil map:")
    val soilToFertilizer = input.extractTransformationFor("soil-to-fertilizer map:")
    val fertilizerToWater = input.extractTransformationFor("fertilizer-to-water map:")
    val waterToLightMap = input.extractTransformationFor("water-to-light map:")
    val lightToTemperatureMap = input.extractTransformationFor("light-to-temperature map:")
    val temperatureToHumidityMap = input.extractTransformationFor("temperature-to-humidity map:")
    val humidityToLocationMap = input.extractTransformationFor("humidity-to-location map:")

    return { num: Long ->
        num
            .let(seedToSoilMap)
            .let(soilToFertilizer)
            .let(fertilizerToWater)
            .let(waterToLightMap)
            .let(lightToTemperatureMap)
            .let(temperatureToHumidityMap)
            .let(humidityToLocationMap)
    }
}

fun List<String>.extractTransformationFor(text: String): (Long) -> Long {
    return dropWhile { it != text }
        .drop(1)
        .takeWhile { it.isNotBlank() }
        .map {
            val (destination, source, amount) = it.split(" ").map { it.toLong() };

            { num: Long ->
                if (num in source..source + amount) {
                    destination + (num - source)
                } else {
                    num
                }
            }
        }
        .let {
            { num ->
                var transformed = num
                for (transformation in it) {
                    transformed = transformation(num)
                    if (transformed != num) {
                        break
                    }
                }

                transformed
            }
        }
}

