package eu.janvdb.aoc2022.day16

import eu.janvdb.aocutil.kotlin.readLines

//const val FILENAME = "input16-test.txt"
const val FILENAME = "input16.txt"


/*
 * Part 2 gives the correct result for the full input, but not for the test input.
 * Probably the elephant needs enough empty nodes to operate independently.
 */

fun main() {
	val valvesList = readLines(2022, FILENAME).map(String::toValve)
	val valves = Valves.create(valvesList)

	part1(valves)
	part2(valves)
}

fun part1(valves: Valves) {
	val best = findBestPath(valves, 30, valves.names, 1)
	println("$best ${best?.totalFlow}")
}

fun part2(valves: Valves) {
	val best = findBestPath(valves, 26, valves.names, 2)
	println("$best ${best?.totalFlow}")
}

fun findBestPath(valves: Valves, minutes: Int, valvesToUse: Set<String>, actorsLeft: Int): Solution? {
	if (actorsLeft == 0) return null

	val queue = mutableListOf(Path(listOf(START_VALVE), 0, 0))
	var best: Solution? = null

	while (!queue.isEmpty()) {
		val currentPath = queue.removeLast()

		val lastStep = currentPath.steps.last()

		val nextSteps = valvesToUse.asSequence()
			.filter { !currentPath.steps.contains(it) }
			.map { next ->
				val stepLength = valves.stepLength(lastStep, next) + 1
				val flow = valves.flowRate(next) * (minutes - currentPath.length - stepLength)
				currentPath.addStep(next, stepLength, flow)
			}
			.filter { it.length <= minutes }
			.toList()
		queue.addAll(nextSteps)

		if (nextSteps.isEmpty()) {
			val solutionWithOtherActors = findBestPath(valves, minutes, valvesToUse - currentPath.steps, actorsLeft - 1)
			val currentSolution = solutionWithOtherActors?.with(currentPath) ?: Solution(listOf(currentPath))

			if (best == null || best.totalFlow < currentSolution.totalFlow) best = currentSolution
		}
	}

	return best
}

data class Path(val steps: List<String>, val length: Int, val totalFlow: Int) {
	fun addStep(step: String, stepLength: Int, flow: Int) =
		Path(steps + step, length + stepLength, totalFlow + flow)
}

data class Solution(val paths: List<Path>) {
	val totalFlow = paths.sumOf { it.totalFlow }

	fun with(path: Path): Solution {
		val newPaths = paths.toMutableList()
		newPaths.add(0, path)
		return Solution(newPaths)
	}
}