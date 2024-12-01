package eu.janvdb.aoc2023.day24

import eu.janvdb.aocutil.kotlin.point2d.Point2DDouble
import eu.janvdb.aocutil.kotlin.point3d.Point3DDouble
import eu.janvdb.aocutil.kotlin.readLines

//const val FILENAME = "input24-test.txt"
//const val MIN_INTERSECT = 7.0
//const val MAX_INTERSECT = 27.0
const val FILENAME = "input24.txt"
const val MIN_INTERSECT = 200000000000000.0
const val MAX_INTERSECT = 400000000000000.0

fun main() {
	val hailstones = readLines(2023, FILENAME).map { Hailstone.parse(it) }

	part1(hailstones)
}

private fun part1(hailstones: List<Hailstone>) {
	val pairs = hailstones.indices
		.flatMap { index1 ->
			(index1 + 1..<hailstones.size).map { index2 -> Pair(hailstones[index1], hailstones[index2]) }
		}
	val intersections = pairs.map { it.first.intersectWithXY(it.second) }
	val validIntersections = intersections.filter { it != null && it.valid }

	println(validIntersections.count())
}

data class Hailstone(val origin: Point3DDouble, val velocity: Point3DDouble) {
	private val m = if (velocity.x != 0.0) velocity.y / velocity.x else null
	private val q = if (m != null) origin.y - m * origin.x else origin.y

	fun intersectWithXY(other: Hailstone): IntersectionXY? {
		if (m == other.m) return null

		if (m == null) {
			val point = Point2DDouble((q - other.q) / other.m!!, q)
			return IntersectionXY(point, getTime(point), other.getTime(point))
		}

		if (other.m == null) {
			return other.intersectWithXY(this)
		}

		val x = (other.q - q) / (m - other.m)
		val y = m * x + q
		val point = Point2DDouble(x, y)
		return IntersectionXY(point, getTime(point), other.getTime(point))
	}

	private fun getTime(point: Point2DDouble): Double {
		return if (velocity.x != 0.0) (point.x - origin.x) / velocity.x else (point.y - origin.y) / velocity.y
	}

	companion object {
		private val REGEX = Regex("[^0-9-]+")
		fun parse(line: String): Hailstone {
			val numbers = line.split(REGEX).map(String::toDouble)
			val origin = Point3DDouble(numbers[0], numbers[1], numbers[2])
			val velocity = Point3DDouble(numbers[3], numbers[4], numbers[5])
			return Hailstone(origin, velocity)
		}
	}
}

data class IntersectionXY(val point: Point2DDouble, val time1: Double, val time2: Double) {
	val valid = time1 >= 0.0 && time2 >= 0 && point.x in MIN_INTERSECT..MAX_INTERSECT && point.y in MIN_INTERSECT..MAX_INTERSECT
}