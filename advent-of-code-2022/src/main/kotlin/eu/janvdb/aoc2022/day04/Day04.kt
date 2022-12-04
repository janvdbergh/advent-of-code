package eu.janvdb.aoc2022.day04

import eu.janvdb.aocutil.kotlin.readLines

//const val FILENAME = "input04-test.txt"
const val FILENAME = "input04.txt"

fun main() {
	val pairs = readLines(2022, FILENAME)
		.map(String::toElfPair)

	val part1 = pairs.count { it.fullyOverlaps() }
	println(part1)

	val part2 = pairs.count { it.overlaps() }
	println(part2)
}

data class SectionRange(val from: Int, val to: Int) {
	fun fullyOverlapsWith(other: SectionRange): Boolean {
		if (this.from>=other.from && this.to<=other.to) return true
		if (other.from>=this.from && other.to<=this.to) return true
		return false
	}
	fun overLapsWith(other: SectionRange): Boolean {
		if (this.to < other.from) return false
		if (other.to < this.from) return false
		return true
	}
}

data class ElfPair(val first: SectionRange, val second: SectionRange) {
	fun fullyOverlaps(): Boolean = first.fullyOverlapsWith(second)
	fun overlaps(): Boolean = first.overLapsWith(second)
}

fun String.toSectionRange(): SectionRange {
	val parts = this.split("-")
	return SectionRange(parts[0].toInt(), parts[1].toInt())
}

fun String.toElfPair(): ElfPair {
	val parts = this.split(",")
	return ElfPair(parts[0].toSectionRange(), parts[1].toSectionRange())
}