package eu.janvdb.aoc2021.day06

import eu.janvdb.aocutil.kotlin.readLines

const val FILENAME = "input06.txt"

const val SPAWN_DELAY = 7
const val INITIAL_DELAY = 2

fun main() {
	val state = State.readFromInput()

	// Part 1
	runDays(state, 80)

	// Part 2
	runDays(state, 256)
}

private fun runDays(initialState: State, numberOfDays: Int) {
	var state = initialState
	println("0\t${state.totalFish()}\t${state.fishPerDay}")
	for (i in 1..numberOfDays) {
		state = state.next()
		println("$i\t${state.totalFish()}\t${state.fishPerDay}")
	}
}

data class State(val fishPerDay: Map<Int, Long>) {

	fun totalFish() = fishPerDay.values.sum()

	fun next(): State {
		val newFishPerDay = mutableMapOf<Int, Long>()

		fun addFishPerDay(day: Int, fish: Long) {
			newFishPerDay[day] = newFishPerDay.getOrDefault(day, 0) + fish
		}

		fishPerDay.forEach { entry ->
			if (entry.key == 0) {
				addFishPerDay(SPAWN_DELAY - 1, entry.value)
				addFishPerDay(SPAWN_DELAY + INITIAL_DELAY - 1, entry.value)
			} else {
				addFishPerDay(entry.key - 1, entry.value)
			}
		}

		return State(newFishPerDay)
	}

	companion object {
		fun readFromInput(): State {
			val input = readLines(2021, FILENAME)
				.flatMap { it.split(",").map { it.toInt() } }
				.groupBy { it }
				.map { Pair(it.key, it.value.size.toLong()) }
				.toMap()
			return State(input)
		}
	}
}