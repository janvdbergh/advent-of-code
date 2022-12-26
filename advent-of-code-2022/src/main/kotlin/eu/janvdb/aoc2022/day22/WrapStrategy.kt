package eu.janvdb.aoc2022.day22

fun interface WrapStrategy {
	fun wrap(originalPosition: Position, positionToWrap: Position): Position
}