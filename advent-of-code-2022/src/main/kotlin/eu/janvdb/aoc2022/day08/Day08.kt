package eu.janvdb.aoc2022.day08

import eu.janvdb.aocutil.kotlin.readLines

//const val FILENAME = "input08-test.txt"
const val FILENAME = "input08.txt"

fun main() {
	val matrix = IntMatrix.parseFromDigits(readLines(2022, FILENAME))
	println(matrix)
	println(matrix.countVisibile())

	println(matrix.maxScenicScore())
}

