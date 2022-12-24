package eu.janvdb.aoc2022.day24

import eu.janvdb.aocutil.kotlin.findShortestPath
import eu.janvdb.aocutil.kotlin.point2d.Point2D
import eu.janvdb.aocutil.kotlin.readLines

//const val FILENAME = "input24-test.txt"
//const val FILENAME = "input24-test2.txt"
const val FILENAME = "input24.txt"

fun main() {
	val blizzardMap = readLines(2022, FILENAME).toBlizzardMap()
	val blizzardMaps = createAllMaps(blizzardMap)

	val atEnd: (State) -> Boolean = { it.position.y >= blizzardMap.height }
	val atStart: (State) -> Boolean = { it.position.y < 0 }

	val start = State(blizzardMaps, 0, Point2D(0, -1))
	val shortest1 = findShortestPath(start, atEnd, State::nextMoves) ?: throw IllegalStateException("No solution found")
	val shortest2 = findShortestPath(shortest1.state, atStart, State::nextMoves) ?: throw IllegalStateException("No solution found")
	val shortest3 = findShortestPath(shortest2.state, atEnd, State::nextMoves) ?: throw IllegalStateException("No solution found")

	println("${shortest1.cost} + ${shortest2.cost} + ${shortest3.cost} = ${shortest1.cost + shortest2.cost + shortest3.cost}" )
}

private fun createAllMaps(blizzardMap: BlizzardMap): MutableList<BlizzardMap> {
	val maps = mutableListOf(blizzardMap)
	var currentMap = blizzardMap
	while (true) {
		currentMap = currentMap.move()
		if (maps[0] == currentMap) break
		maps.add(currentMap)
	}
	return maps
}