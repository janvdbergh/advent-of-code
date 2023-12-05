package eu.janvdb.aoc2023.day05

import eu.janvdb.aocutil.kotlin.CombinedRange
import eu.janvdb.aocutil.kotlin.SimpleRange
import eu.janvdb.aocutil.kotlin.readGroupedLines
import java.util.*

//const val FILENAME = "input05-test.txt"
const val FILENAME = "input05.txt"

fun main() {
    val groupedLines = readGroupedLines(2023, FILENAME)

    part1(groupedLines)
    part2(groupedLines)
}

private fun part1(groupedLines: List<List<String>>) {
    val seeds = parseSingleRanges(groupedLines[0][0])
    val mappings = Mappings.parse(groupedLines)
    val seedsMapped = mappings.map(seeds)

    println(seedsMapped.min().start)
}

private fun part2(groupedLines: List<List<String>>) {
    val seeds = parseRanges(groupedLines[0][0])
    val mappings = Mappings.parse(groupedLines)
    val seedsMapped = mappings.map(seeds)

    println(seedsMapped.min().start)
}

fun parseSingleRanges(line: String): CombinedRange {
    return line.split(": ")[1].split(" ")
        .map { it.toLong() }
        .map { SimpleRange(it, it) }
        .fold(CombinedRange()) { acc, range -> acc.add(range) }
}

fun parseRanges(line: String): CombinedRange {
    return line.split(": ")[1].split(" ")
        .map { it.toLong() }
        .chunked(2) { SimpleRange(it[0], it[0] + it[1] - 1) }
        .fold(CombinedRange()) { acc, range -> acc.add(range) }
}

data class Mappings(val mappings: List<Mapping>) {
    fun map(source: CombinedRange): CombinedRange {
        return mappings.fold(source) { acc, mapping -> mapping.map(acc) }
    }

    companion object {
        fun parse(groupedLines: List<List<String>>): Mappings {
            val maps = groupedLines.drop(1).map { Mapping.parse(it) }
            return Mappings(maps)
        }
    }
}


data class Mapping(val name: String, val entries: List<MappingEntry>) {

    fun map(rangeSet: CombinedRange): CombinedRange {
        val todo = LinkedList(rangeSet.ranges)
        var result = CombinedRange()
        while (!todo.isEmpty()) {
            val range = todo.removeFirst()
            if (range.isEmpty()) continue

            val overlap = entries.find { it.sourceRange.overlapsWith(range) }
            if (overlap == null) {
                result = result.add(range)
            } else {
                result = result.add(
                    range.intersectWith(overlap.sourceRange).move(overlap.destinationStart - overlap.sourceRange.start)
                )
                range.subtract(overlap.sourceRange).forEach { todo.add(it) }
            }
        }

        println(result)
        return result
    }

    companion object {
        fun parse(lines: List<String>): Mapping {
            val name = lines[0]
            val entries = lines.drop(1)
                .map { MappingEntry.parse(it) }
                .sortedBy { it.sourceRange.start }
            return Mapping(name, entries)
        }
    }
}

data class MappingEntry(val sourceRange: SimpleRange, val destinationStart: Long) {
    companion object {
        fun parse(line: String): MappingEntry {
            val parts = line.split(" ").map { it.toLong() }
            val sourceRange = SimpleRange(parts[1], parts[2] + parts[1] - 1)
            return MappingEntry(sourceRange, parts[0])
        }
    }
}
