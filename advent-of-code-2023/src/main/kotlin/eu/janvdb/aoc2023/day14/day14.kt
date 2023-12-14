package eu.janvdb.aoc2023.day14

import eu.janvdb.aocutil.kotlin.readLines

//const val FILENAME = "input14-test.txt"
const val FILENAME = "input14.txt"

const val NUMBER_OF_ITERATIONS = 1000000000

fun main() {
	val platform = readLines(2023, FILENAME).let { Platform.parse(it) }
	part1(platform)
	part2(platform)
}

private fun part1(platform: Platform) {
	val shifted = platform.shiftUp()
	println(shifted.weight())
}

fun part2(platform: Platform) {
	val cyclesPerPlatform = mutableMapOf<Platform, Int>()
	val weights = mutableListOf<Int>()

	var current = platform
	var cycles = 0
	while (true) {
		current = current.cycle()

		if (cyclesPerPlatform.containsKey(current)) {
			break
		}

		val weight = current.weight()
		cyclesPerPlatform[current] = cycles++
		weights.add(weight)
	}

	val initial = cyclesPerPlatform[current]!!
	val repetition = cycles - initial
	val index = (NUMBER_OF_ITERATIONS - initial - 1) % repetition + initial
	println(weights[index])
}

enum class Rock(val ch: Char) {
	ROUND('O'),
	CUBE('#'),
	NONE('.');

	companion object {
		fun parse(ch: Char) = entries.first { it.ch == ch }
	}
}

data class Platform(val size: Int, val rocks: List<Rock>) {
	fun shiftUp(): Platform {
		val newRocks = rocks.toMutableList()

		fun get(x: Int, y: Int) = newRocks[index(x, y)]
		fun set(x: Int, y: Int, rock: Rock) {
			newRocks[index(x, y)] = rock
		}

		for (y in 0 until size) {
			for (x in 0 until size) {
				if (get(x, y) == Rock.ROUND) {
					var newY = y
					while (newY > 0 && get(x, newY - 1) == Rock.NONE) newY--
					set(x, y, Rock.NONE)
					set(x, newY, Rock.ROUND)
				}
			}
		}

		return Platform(size, newRocks)
	}

	private fun rotateRight(): Platform {
		val newRocks = (0..<size).flatMap { y ->
			(0..<size).map { x -> get(y, size - x - 1) }
		}
		return Platform(size, newRocks)
	}

	fun cycle(): Platform {
		var current = this
		for (i in 1..4) {
			current = current.shiftUp().rotateRight()
		}
		return current
	}

	fun weight(): Int {
		return (0..<size).sumOf { y ->
			(0..<size).count { x -> get(x, y) == Rock.ROUND } * (size - y)
		}
	}

	fun print() {
		for (y in 0 until size) {
			for (x in 0 until size) {
				print(get(x, y).ch)
			}
			println()
		}
		println()
	}

	fun get(x: Int, y: Int) = rocks[index(x, y)]

	private fun index(x: Int, y: Int) = y * size + x

	companion object {
		fun parse(input: List<String>): Platform {
			val size = input[0].length
			val rocks = input.flatMap { line -> line.toCharArray().map { Rock.parse(it) } }
			return Platform(size, rocks)
		}
	}
}