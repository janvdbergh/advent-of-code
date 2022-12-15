package eu.janvdb.aoc2022.day15

import eu.janvdb.aocutil.kotlin.readLines

//const val FILENAME = "input15-test.txt"
//const val LINE = 10
//val SEARCH_REGION = 0..20
const val FILENAME = "input15.txt"
const val LINE = 2000000
val SEARCH_REGION = 0..4_000_000

fun main() {
	val sensorBeacons = readLines(2022, FILENAME).toSensorBeacons()
	part1(sensorBeacons)
	part2(sensorBeacons)
}

private fun part1(sensorBeacons: SensorBeacons) {
	val count = sensorBeacons.countPointsWhereNoBeaconCanBe(LINE)
	println(count)
}

private fun part2(sensorBeacons: SensorBeacons) {
	SEARCH_REGION.asSequence()
		.flatMap { y -> sensorBeacons.findPointsWhereBeaconCanBe(y, SEARCH_REGION)}
		.forEach { println(it.x * 4000000L + it.y) }
}


