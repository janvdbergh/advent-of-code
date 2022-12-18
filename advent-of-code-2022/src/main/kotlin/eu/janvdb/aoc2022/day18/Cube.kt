package eu.janvdb.aoc2022.day18

data class Cube(val x: Int, val y: Int, val z: Int) {
	fun getSides(): Sequence<Side> {
		return sequenceOf(
			Side(x + 0, x + 0, y + 0, y + 1, z + 0, z + 1),
			Side(x + 1, x + 1, y + 0, y + 1, z + 0, z + 1),
			Side(x + 0, x + 1, y + 0, y + 0, z + 0, z + 1),
			Side(x + 0, x + 1, y + 1, y + 1, z + 0, z + 1),
			Side(x + 0, x + 1, y + 0, y + 1, z + 0, z + 0),
			Side(x + 0, x + 1, y + 0, y + 1, z + 1, z + 1),
		)
	}
}

data class Side(val xFrom: Int, val xTo: Int, val yFrom: Int, val yTo: Int, val zFrom: Int, val zTo: Int)

fun String.toCube(): Cube {
	val parts = split(",")
	return Cube(parts[0].toInt(), parts[1].toInt(), parts[2].toInt())
}

fun Iterable<Cube>.min(f: (Cube) -> Int) = f(minBy(f))
fun Iterable<Cube>.max(f: (Cube) -> Int) = f(maxBy(f))
fun Iterable<Cube>.rangeOver(f: (Cube) -> Int) = IntRange(min(f) + 1, max(f) - 1)
