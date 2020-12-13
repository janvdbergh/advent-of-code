package eu.janvdb.aoc2020.day13

const val TIME = 1000052L
const val BUSSES = "23,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,37,x,x,x,x,x,863,x,x,x,x,x,x,x,x,x,x,x,19,13,x,x,x,17,x,x,x,x,x,x,x,x,x,x,x,29,x,571,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,41"

fun main() {
	part1()
	part2()
}

private fun part1() {
	val busses = BUSSES.split(",").filter { it != "x" }.map(String::toInt)
	val earliestStartTimesPerBus = busses.map { Pair(it, (TIME + it - 1) / it * it) }
	val earliestBus = earliestStartTimesPerBus.minByOrNull { it.second }!!

	println(earliestBus)
	println(earliestBus.first * (earliestBus.second - TIME))
}

private fun part2() {
	val startDeltas = BUSSES.split(",")
		.mapIndexed { index, value -> Pair(index, value) }
		.filter { it.second != "x" }
		.map { StartDelta(it.second.toLong(), it.first.toLong()) }

	println(startDeltas)

	// Equations: (t + 0) mod 7 = 0, (t + 1) mod 13 = 0, (t + 4) mod 59 = 0, (t + 6) mod 31 = 0, (t + 7) mod 19 = 0
	// Or: t = 0 (mod 7), t = 12 (mod 13), t = 54 (mod 59), t = 25 (mod 31), t = 12 (mod 19)
	// Since the values are pairwise coprime, this can be solved with the Chinese Remainder Theorem.

	// Equation solved for now with Wolfram Alpha.
}

data class StartDelta(val bus: Long, val startTime: Long) {
	override fun toString(): String {
		return "(t + $startTime) mod $bus = 0"
	}
}
