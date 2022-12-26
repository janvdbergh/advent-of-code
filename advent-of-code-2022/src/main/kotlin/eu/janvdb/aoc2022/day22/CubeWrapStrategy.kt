package eu.janvdb.aoc2022.day22

import eu.janvdb.aocutil.kotlin.point2d.Point2D

class CubeWrapStrategy(val facedMap: FacedMap, val cubeLinks: List<CubeLink>) : WrapStrategy {
	override fun wrap(originalPosition: Position, positionToWrap: Position): Position {
		val link = cubeLinks.find { it.matches(facedMap, originalPosition) } ?: throw noLinkFound(originalPosition)
		return link.applyTo(facedMap, originalPosition)
	}

	private fun noLinkFound(originalPosition: Position): IllegalStateException {
		val message = "No link found for $originalPosition\n" +
				"CubeLink(Point2D(${originalPosition.x/facedMap.dimension}, ${originalPosition.y/facedMap.dimension}), Direction.${originalPosition.direction}, Point2D(?, ?), Direction.?),"
		return IllegalStateException(message)
	}
}

data class CubeLink(
	val fromFace: Point2D, val fromDirection: Direction,
	val toFace: Point2D, val toDirection: Direction
) {
	fun matches(facedMap: FacedMap, originalPosition: Position) =
		fromFace.x == originalPosition.x / facedMap.dimension &&
				fromFace.y == originalPosition.y / facedMap.dimension &&
				fromDirection == originalPosition.direction

	fun applyTo(facedMap: FacedMap, originalPosition: Position): Position {
		val dimension = facedMap.dimension

		fun invert(xy: Int): Int = dimension - xy - 1

		val xInOriginalFace = originalPosition.x % dimension
		val yInOriginalFace = originalPosition.y % dimension

		val newXY = when (fromDirection) {
			Direction.LEFT -> when (toDirection) {
				Direction.LEFT -> Point2D(dimension - 1, yInOriginalFace)
				Direction.RIGHT -> Point2D(0, invert(yInOriginalFace))
				Direction.DOWN -> Point2D(yInOriginalFace, 0)
				Direction.UP -> Point2D(invert(yInOriginalFace), dimension - 1)
			}

			Direction.RIGHT -> when (toDirection) {
				Direction.LEFT -> Point2D(dimension - 1, invert(yInOriginalFace))
				Direction.RIGHT -> Point2D(0, yInOriginalFace)
				Direction.DOWN -> Point2D(invert(yInOriginalFace), 0)
				Direction.UP -> Point2D(yInOriginalFace, dimension - 1)
			}

			Direction.DOWN -> when (toDirection) {
				Direction.LEFT -> Point2D(dimension - 1, xInOriginalFace)
				Direction.RIGHT -> Point2D(0, invert(xInOriginalFace))
				Direction.DOWN -> Point2D(xInOriginalFace, 0)
				Direction.UP -> Point2D(invert(xInOriginalFace), dimension - 1)
			}

			Direction.UP -> when (toDirection) {
				Direction.LEFT -> Point2D(dimension - 1, invert(xInOriginalFace))
				Direction.RIGHT -> Point2D(0, xInOriginalFace)
				Direction.DOWN -> Point2D(invert(xInOriginalFace), 0)
				Direction.UP -> Point2D(xInOriginalFace, dimension - 1)
			}
		}

		return Position(toFace.x * dimension + newXY.x, toFace.y * dimension + newXY.y, toDirection)
	}
}