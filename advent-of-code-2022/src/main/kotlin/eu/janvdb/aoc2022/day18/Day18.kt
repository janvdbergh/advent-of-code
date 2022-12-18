package eu.janvdb.aoc2022.day18

import eu.janvdb.aocutil.kotlin.readLines

//const val FILENAME = "input18-test.txt"
//const val FILENAME = "input18-test2.txt"
const val FILENAME = "input18.txt"

fun main() {
	val cubes = readLines(2022, FILENAME).map(String::toCube).toCubes()
	println(cubes.uniqueSides.size)

	val airPockets = cubes.findAirPockets()
	val remainingSides = cubes.uniqueSides - airPockets.uniqueSides
	println(remainingSides.size)
}



