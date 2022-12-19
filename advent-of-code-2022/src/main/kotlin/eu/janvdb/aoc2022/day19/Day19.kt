package eu.janvdb.aoc2022.day19

import eu.janvdb.aocutil.kotlin.readLines
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import java.util.concurrent.atomic.AtomicInteger

//const val FILENAME = "input19-test.txt"
const val FILENAME = "input19.txt"

fun main() {
	val blueprints = readLines(2022, FILENAME).map(String::toBluePrint)

	part1(blueprints)
	part2(blueprints.take(3))
}

private fun part1(blueprints: List<Blueprint>) {
	val sum = AtomicInteger()
	runBlocking {
		val tasks = blueprints
			.map { blueprint ->
				async(Dispatchers.Default) {
					sum.addAndGet(
						blueprint.number * findBestResult(
							blueprint,
							24
						)
					)
				}
			}
		tasks.awaitAll()
	}

	println("Result: $sum")
}

private fun part2(blueprints: List<Blueprint>) {
	val product = AtomicInteger(1)
	runBlocking {
		val tasks = blueprints
			.map { blueprint ->
				async(Dispatchers.Default) {
					product.accumulateAndGet(findBestResult(blueprint, 32)) { x, y -> x * y }
				}
			}
		tasks.awaitAll()
	}

	println("Result: $product")
}

fun findBestResult(blueprint: Blueprint, minutes: Int): Int {
	println("Starting for ${blueprint.number} on thread ${Thread.currentThread().name}")

	var states = listOf(initialState(blueprint, minutes))

	repeat(minutes) {
		states = states.flatMap { it.getNextStates() }
			.sortedBy { -it.score }
			.take(15000)
	}

	val bestState = states[0]
	println("Finished for ${blueprint.number} on thread ${Thread.currentThread().name} with result $bestState")
	return bestState.materials.geodes
}

fun initialState(bluePrint: Blueprint, minutes: Int) =
	State(minutes, Materials(0, 0, 0, 0), Materials(1, 0, 0, 0), bluePrint)