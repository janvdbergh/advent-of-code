package eu.janvdb.aoc2024.day25

import eu.janvdb.aocutil.kotlin.readGroupedLines

//const val FILENAME = "input25-test.txt"
const val FILENAME = "input25.txt"

const val WIDTH = 5
const val HEIGHT = 6

fun main() {
	val lines = readGroupedLines(2024, FILENAME)
	val locks = lines.filter { isLock(it) }.map { Lock.parse(it) }
	val keys = lines.filter { !isLock(it) }.map { Key.parse(it) }

	val result = locks.sumOf { lock -> keys.count { key -> lock.fits(key) } }
	println(result)
}

fun isLock(lines: List<String>) = lines[0].matches(Regex("^#+$"))

data class Lock(val heights: List<Int>) {

	fun fits(key: Key): Boolean {
		return (0 until WIDTH).all { heights[it] + key.heights[it] < HEIGHT }
	}

	companion object {
		fun parse(lines: List<String>): Lock {
			val heights = Array(WIDTH) { 0 }
			for (x in 0 until WIDTH) {
				for (y in 0..HEIGHT) {
					if (lines[y][x] != '#') break
					heights[x] = y
				}
			}
			return Lock(heights.toList())
		}
	}
}

data class Key(val heights: List<Int>) {
	companion object {
		fun parse(lines: List<String>): Key {
			val heights = Array(WIDTH) { 0 }
			for (x in 0 until WIDTH) {
				for (y in 0..HEIGHT) {
					if (lines[HEIGHT - y][x] != '#') break
					heights[x] = y
				}
			}
			return Key(heights.toList())
		}
	}
}