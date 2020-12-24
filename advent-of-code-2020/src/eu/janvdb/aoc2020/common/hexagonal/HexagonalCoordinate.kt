package eu.janvdb.aoc2020.common.hexagonal

class HexagonalCoordinate internal constructor(val x: Int, val y: Int) {

	fun add(other: HexagonalCoordinate, times: Int = 1): HexagonalCoordinate {
		return HexagonalCoordinate(x + other.x * times, y + other.y * times)
	}

	fun add(direction: HexagonalDirection, times: Int = 1): HexagonalCoordinate {
		return add(direction.coordinate, times)
	}

	fun neighbours(): Sequence<HexagonalCoordinate> {
		return sequenceOf(
			add(HexagonalDirection.W),
			add(HexagonalDirection.NW),
			add(HexagonalDirection.NE),
			add(HexagonalDirection.E),
			add(HexagonalDirection.SE),
			add(HexagonalDirection.SW)
		)
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false
		other as HexagonalCoordinate
		return x == other.x && y == other.y
	}

	override fun hashCode(): Int {
		return 31 * x + y
	}

	override fun toString(): String {
		return "($x, $y)"
	}

	companion object {
		val ORIGIN = HexagonalCoordinate(0, 0)

		fun iterate(from: HexagonalCoordinate, to: HexagonalCoordinate): Sequence<HexagonalCoordinate> {
			fun xRange(y: Int): IntProgression {
				val evenLine = y % 2 == 0
				return if (evenLine) {
					val minX = if (from.x % 2 == 0) from.x else from.x - 1
					val maxX = if (to.x % 2 == 0) to.x else to.x + 1
					(minX..maxX step 2)
				} else {
					val minX = if (from.x % 2 == 0) from.x - 1 else from.x
					val maxX = if (to.x % 2 == 0) to.x + 1 else to.x
					(minX..maxX step 2)
				}
			}

			return (from.y..to.y).asSequence().flatMap { y ->
				xRange(y).asSequence().map { x -> HexagonalCoordinate(x, y) }
			}
		}
	}
}

fun Collection<HexagonalCoordinate>.topLeft(): HexagonalCoordinate {
	val minX = minOf { it.x }
	val minY = minOf { it.y }
	return if ((minX + minY) % 2 == 0) HexagonalCoordinate(minX, minY) else HexagonalCoordinate(minX - 1, minY)
}

fun Collection<HexagonalCoordinate>.bottomRight(): HexagonalCoordinate {
	val maxX = maxOf { it.x }
	val maxY = maxOf { it.y }
	return if ((maxX + maxY) % 2 == 0) HexagonalCoordinate(maxX, maxY) else HexagonalCoordinate(maxX + 1, maxY)
}
