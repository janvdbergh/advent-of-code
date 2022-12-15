package eu.janvdb.aoc2022.day15

import eu.janvdb.aocutil.kotlin.point2d.Point2D
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

val PATTERN = Regex("Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)")

data class SensorBeacon(val sensor: Point2D, val beacon: Point2D) {
	private val distance = sensor.manhattanDistanceTo(beacon)
	val minX = sensor.x - distance
	val maxX = sensor.x + distance

	fun getRangeForLine(line: Int): Range {
		val offset = distance - abs(line - sensor.y)
		return Range(sensor.x - offset, sensor.x + offset)
	}
}

fun String.toSensorBeacon(): SensorBeacon {
	val matchResult = PATTERN.matchEntire(this) ?: throw IllegalArgumentException(this)
	return SensorBeacon(
		Point2D(matchResult.groupValues[1].toInt(), matchResult.groupValues[2].toInt()),
		Point2D(matchResult.groupValues[3].toInt(), matchResult.groupValues[4].toInt())
	)
}

data class SensorBeacons(val sensorBeacons: List<SensorBeacon>) {
	private val minX = sensorBeacons.minOfOrNull { it.minX }!!
	private val maxX = sensorBeacons.maxOfOrNull { it.maxX }!!
	private val beacons = sensorBeacons.map { it.beacon }.toSet()

	fun countPointsWhereNoBeaconCanBe(line: Int): Int {
		val positionWhereBeaconsCanBe = getIntervalWhereBeaconCanBe(line, Range(minX, maxX)).size()
		val beaconsOnLine = beacons.count { it.y == line }
		return maxX - minX + 1 - positionWhereBeaconsCanBe - beaconsOnLine
	}

	fun findPointsWhereBeaconCanBe(line: Int, searchRegion: IntRange): Set<Point2D> {
		return getIntervalWhereBeaconCanBe(line, Range.from(searchRegion)).getValues()
			.map { Point2D(it, line) }
			.toSet()
	}

	private fun getIntervalWhereBeaconCanBe(line: Int, range: Range): Intervals {
		var intervals = Intervals(listOf(range))
		sensorBeacons.asSequence()
			.map { it.getRangeForLine(line) }
			.filter { !it.isEmpty() }
			.forEach { intervals = intervals.subtract(it) }
		return intervals
	}
}

fun List<String>.toSensorBeacons(): SensorBeacons = SensorBeacons(map(String::toSensorBeacon))


data class Range(val from: Int, val to: Int) {
	fun isEmpty() = to < from
	fun getValues() = IntRange(from, to).asSequence()
	fun size():Int = if (isEmpty()) 0 else to - from + 1

	companion object {
		fun from(intRange: IntRange): Range = Range(intRange.first, intRange.last)
	}
}

data class Intervals(val ranges: List<Range>) {
	fun subtract(range: Range): Intervals {
		val newRanges = ranges.asSequence()
			.flatMap {
				sequenceOf(
					Range(it.from, min(it.to, range.from - 1)),
					Range(max(it.from, range.to + 1), it.to)
				)
			}
			.filter { !it.isEmpty() }
			.toList()
		return Intervals(newRanges)
	}

	fun getValues() = ranges.asSequence().flatMap(Range::getValues)
	fun size(): Int = ranges.sumOf { it.size() }
}