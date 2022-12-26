package eu.janvdb.aoc2022.day22

class WrapAroundWrapStrategy(val facedMap: FacedMap) : WrapStrategy {
	override fun wrap(originalPosition: Position, positionToWrap: Position): Position {
		fun wrap(position: Position) = Position(
			(position.x + facedMap.width) % facedMap.width,
			(position.y + facedMap.height) % facedMap.height,
			position.direction
		)

		var current = wrap(positionToWrap)
		while (facedMap.tile(current.x, current.y) == Tile.NOTHING) {
			current = wrap(current.step())
		}
		return current
	}
}