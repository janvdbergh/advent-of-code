package eu.janvdb.aoc2020.day11

import eu.janvdb.aocutil.kotlin.readLines

const val INPUT = "input11.txt"

fun main() {
	val seating = readSeating()
	seating.print()

	executeSteps(seating, Seating::step1)
	executeSteps(seating, Seating::step2)
}

fun readSeating(): Seating {
	return Seating(readLines(2020, INPUT))
}

private fun executeSteps(seating: Seating, stepFunction: (Seating) -> Seating) {
	var previous = seating
	var current = stepFunction.invoke(previous)
	current.print()

	while (previous != current) {
		previous = current
		current = stepFunction.invoke(previous)
		current.print()
	}
}
