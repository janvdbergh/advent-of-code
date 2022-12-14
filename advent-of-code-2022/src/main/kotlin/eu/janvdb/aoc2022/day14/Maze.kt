package eu.janvdb.aoc2022.day14

import eu.janvdb.aocutil.kotlin.point2d.Point2D

val SAND_START = Point2D(500, 0)
const val OFFSET_INFINITE_FLOOR = 2

class Maze(private val walls: Set<Point2D>, private val infiniteFloor: Boolean) {
	private val minY = walls.minOfOrNull { it.y }!!
	private val maxY = walls.maxOfOrNull { it.y }!!
	private val sand = mutableSetOf<Point2D>()

	fun dropSand(): Boolean {
		fun isClear(point: Point2D): Boolean {
			if (walls.contains(point)) return false
			if (sand.contains(point)) return false
			if (infiniteFloor && point.y == maxY + OFFSET_INFINITE_FLOOR) return false
			return true
		}

		fun getNextPosition(current: Point2D): Point2D? {
			val down = current.down()
			if (isClear(down)) return down
			val downLeft = down.left()
			if (isClear(downLeft)) return downLeft
			val downRight = down.right()
			if (isClear(downRight)) return downRight
			return null
		}

		if (sand.contains(SAND_START)) return false

		var current = SAND_START
		var next = getNextPosition(current)
		while (next != null && current.y < maxY + OFFSET_INFINITE_FLOOR) {
			current = next
			next = getNextPosition(current)
		}

		val canAddSand = next == null
		if (canAddSand) sand.add(current)
		return canAddSand
	}

	override fun toString(): String {
		val all = walls + sand
		val minX = all.minOfOrNull { it.x }!!
		val maxX = all.maxOfOrNull { it.x }!!

		val result = StringBuilder()
		for (y in minY..maxY + 1) {
			for (x in minX..maxX) {
				val point = Point2D(x, y)
				result.append(
					when {
						walls.contains(point) -> '#'
						sand.contains(point) -> 'o'
						else -> ' '
					}
				)
			}
			result.append('\n')
		}
		if (infiniteFloor) {
			for (x in minX..maxX) result.append('#')
			result.append('\n')
		}
		return result.toString()
	}

	companion object {
		fun parse(instructions: List<Instruction>, infiniteFloor: Boolean): Maze {
			val walls = instructions.asSequence()
				.flatMap(Instruction::getWalls)
				.toSet()

			return Maze(walls, infiniteFloor)
		}
	}
}

