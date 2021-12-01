package eu.janvdb.aoc2020.day24

import eu.janvdb.aocutil.kotlin.hexagonal.HexagonalCoordinate
import eu.janvdb.aocutil.kotlin.hexagonal.HexagonalDirection
import eu.janvdb.aocutil.kotlin.hexagonal.bottomRight
import eu.janvdb.aocutil.kotlin.hexagonal.topLeft
import eu.janvdb.aocutil.kotlin.readLines

const val NUMBER_OF_DAYS = 100

fun main() {
	val flippedCoordinates = mutableSetOf<HexagonalCoordinate>()
	readLines(2020, "input24.txt")
		.map(::parseLine)
		.forEach { if (flippedCoordinates.contains(it)) flippedCoordinates.remove(it) else flippedCoordinates.add(it) }

	var floor = HexagonalFloor(flippedCoordinates)
	println("0: ${floor.blackTiles.size}")

	for (i in 0 until NUMBER_OF_DAYS) {
		floor = floor.step()
		println("${i+1}: ${floor.blackTiles.size}")
	}
}

fun parseLine(input: String): HexagonalCoordinate {
	var result = HexagonalCoordinate.ORIGIN
	val inputUpperCase = input.toUpperCase()

	var i = 0
	while (i < input.length) {
		val hexagonalDirection: HexagonalDirection
		if (inputUpperCase[i] == 'E' || inputUpperCase[i] == 'W') {
			hexagonalDirection = HexagonalDirection.valueOf(inputUpperCase.substring(i, i + 1))
			i++
		} else {
			hexagonalDirection = HexagonalDirection.valueOf(inputUpperCase.substring(i, i + 2))
			i += 2
		}

		result = result.add(hexagonalDirection.coordinate)
	}

	return result
}

data class HexagonalFloor(val blackTiles: Set<HexagonalCoordinate>) {
	fun step(): HexagonalFloor {
		fun countBlackNeighbours(coordinate: HexagonalCoordinate): Int {
			return coordinate.neighbours().count(blackTiles::contains)
		}

		fun shouldBeBlack(coordinate: HexagonalCoordinate): Boolean {
			val isBlack = blackTiles.contains(coordinate)
			val neighbours = countBlackNeighbours(coordinate)
			return (isBlack && neighbours in 1..2) || (!isBlack && neighbours == 2)
		}

		val topLeft = blackTiles.topLeft().add(HexagonalDirection.NW, 2)
		val bottomRight = blackTiles.bottomRight().add(HexagonalDirection.SE, 2)
		val newCoordinates = HexagonalCoordinate.iterate(topLeft, bottomRight).filter(::shouldBeBlack).toSet()
		return HexagonalFloor(newCoordinates)
	}
}



