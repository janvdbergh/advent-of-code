package eu.janvdb.aoc2022.day22

import eu.janvdb.aocutil.kotlin.point2d.Point2D
import eu.janvdb.aocutil.kotlin.readGroupedLines

// TEST		ACTUAL
// ..#.		.##
// ###.		.#.
// ..##		##.

//const val FILENAME = "input22-test.txt"
//val CUBE_LINKS = listOf(
//	CubeLink(Point2D(2, 1), Direction.RIGHT, Point2D(3, 2), Direction.DOWN),
//	CubeLink(Point2D(2, 2), Direction.DOWN, Point2D(0, 1), Direction.UP),
//	CubeLink(Point2D(1, 1), Direction.UP, Point2D(2, 0), Direction.RIGHT),
//)

const val FILENAME = "input22.txt"
val CUBE_LINKS = listOf(
	CubeLink(Point2D(0, 2), Direction.LEFT, Point2D(1, 0), Direction.RIGHT),
	CubeLink(Point2D(1, 0), Direction.LEFT, Point2D(0, 2), Direction.RIGHT),

	CubeLink(Point2D(0, 2), Direction.UP, Point2D(1, 1), Direction.RIGHT),
	CubeLink(Point2D(1, 1), Direction.LEFT, Point2D(0, 2), Direction.DOWN),

	CubeLink(Point2D(1, 2), Direction.RIGHT, Point2D(2, 0), Direction.LEFT),
	CubeLink(Point2D(2, 0), Direction.RIGHT, Point2D(1, 2), Direction.LEFT),

	CubeLink(Point2D(1, 1), Direction.RIGHT, Point2D(2, 0), Direction.UP),
	CubeLink(Point2D(2, 0), Direction.DOWN, Point2D(1, 1), Direction.LEFT),

	CubeLink(Point2D(1, 2), Direction.DOWN, Point2D(0, 3), Direction.LEFT),
	CubeLink(Point2D(0, 3), Direction.RIGHT, Point2D(1, 2), Direction.UP),

	CubeLink(Point2D(0, 3), Direction.LEFT, Point2D(1, 0), Direction.DOWN),
	CubeLink(Point2D(1, 0), Direction.UP, Point2D(0, 3), Direction.RIGHT),

	CubeLink(Point2D(2, 0), Direction.UP, Point2D(0, 3), Direction.UP),
	CubeLink(Point2D(0, 3), Direction.DOWN, Point2D(2, 0), Direction.DOWN),
)

fun main() {
	val lineGroups = readGroupedLines(2022, FILENAME)
	val map = lineGroups[0].toMap()
	val instructions = lineGroups[1].flatMap { it.toInstructions() }

	runForMap(map, instructions, WrapAroundWrapStrategy(map))

	println(map.structure())
	runForMap(map, instructions, CubeWrapStrategy(map, CUBE_LINKS))
}

fun runForMap(map: FacedMap, instructions: List<Instruction>, strategy: WrapStrategy) {
	var position = map.initialPosition()
	val trace = mutableListOf(position)

	instructions.forEach {
//		println(map.toString(position))
//		println(position)
		println(it)
		position = map.move(position, it, strategy, trace)
	}

	println(map.toString(trace))
	println(position)
}

