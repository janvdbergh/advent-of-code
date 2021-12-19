package eu.janvdb.aocutil.kotlin.point3d

import kotlin.math.abs

data class Point3D(val x: Int, val y: Int, val z: Int) {
	operator fun plus(other: Point3D): Point3D = Point3D(x + other.x, y + other.y, z + other.z)
	operator fun minus(other: Point3D) = Point3D(x - other.x, y - other.y, z - other.z)

	fun manhattanDistance(other: Point3D) = abs(x - other.x) + abs(y - other.y) + abs(z - other.z)

	companion object {
		fun createCommaSeparated(s: String): Point3D {
			val pair = s.split(",")
			return Point3D(pair[0].trim().toInt(), pair[1].trim().toInt(), pair[2].trim().toInt())
		}
	}
}