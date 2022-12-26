package eu.janvdb.aoc2022.day22

data class Instruction(val steps: Int, val rotation: Rotation?) {
	override fun toString(): String {
		val ch = rotation?.ch ?: ' '
		return "$steps$ch"
	}
}

fun String.toInstructions(): List<Instruction> {
	val result = mutableListOf<Instruction>()
	val regex = Regex("(\\d+)([RL]?)")

	var matchResult = regex.find(this)
	while (matchResult != null) {
		val steps = matchResult.groupValues[1].toInt()
		val rotation = Rotation.parse(matchResult.groupValues[2])
		result.add(Instruction(steps, rotation))

		matchResult = matchResult.next()
	}

	return result
}

enum class Rotation(val ch: Char) {
	LEFT('L'), RIGHT('R');

	fun apply(position: Position) = when (this) {
		LEFT -> position.withDirection(position.direction.rotateLeft())
		RIGHT -> position.withDirection(position.direction.rotateRight())
	}

	companion object {
		fun parse(str: String) =
			if (str == "") null else Rotation.values().find { it.ch == str[0] } ?: throw IllegalArgumentException(str)
	}
}