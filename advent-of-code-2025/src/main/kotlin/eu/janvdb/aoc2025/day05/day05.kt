package eu.janvdb.aoc2025.day05

import eu.janvdb.aocutil.kotlin.readGroupedLines

const val FILENAME = "input05.txt"
//const val FILENAME = "input05-test.txt"

fun main() {
	val lines = readGroupedLines(2025, FILENAME)
	val ranges = lines[0].map { Range.from(it) }
	val ids = lines[1].map { it.toLong() }

	val result1 = ids.count { id -> ranges.any { range -> range.contains(id) } }
	println(result1)

	val combined = ranges.fold(CombinedRange()) { acc, it -> acc.add(it) }
	println(combined)
	println(combined.size())
}

data class Range(val start: Long, val end: Long) {
	val size = end - start + 1

	fun contains(id: Long) = id in start..end

	fun overlaps(range: Range): Boolean {
		return !(end < range.start || range.end < start)
	}

	fun overlapWith(ranges: List<Range>): Range {
		val both = ranges + this
		val start = both.minOf { it.start }
		val end = both.maxOf { it.end }
		return Range(start, end)
	}

	companion object {
		fun from(input: String): Range {
			val parts = input.split('-')
			return Range(parts[0].toLong(), parts[1].toLong())
		}
	}
}

data class CombinedRange(val ranges: List<Range> = listOf()) {
	fun add(range: Range): CombinedRange {
		val overlappingRanges = ranges.filter { it.overlaps(range) }
		if (overlappingRanges.isEmpty()) {
			return CombinedRange(ranges + range)
		}
		return CombinedRange(ranges - overlappingRanges + range.overlapWith(overlappingRanges))
	}

	fun size() = ranges.sumOf { range -> range.size }
}