package eu.janvdb.aocutil.kotlin.point2d

enum class Direction(val ch: Char) {
	N('^'), S('v'), E('>'), W('<');

	fun rotateLeft(amount: Int): Direction {
		var actualAmount = amount % 360
		if (actualAmount < 0) actualAmount += 360
		return when (amount) {
			0 -> this
			90 -> when (this) {
				N -> W
				W -> S
				S -> E
				E -> N
			}
			180 -> when (this) {
				N -> S
				W -> E
				S -> N
				E -> W
			}
			270 -> when (this) {
				N -> E
				E -> S
				S -> W
				W -> N
			}
			else -> throw IllegalArgumentException(amount.toString())
		}
	}

	fun rotateRight(amount: Int): Direction {
		return rotateLeft(360 - amount)
	}

	companion object{
		fun parse(ch: Char): Direction? = Direction.values().find { it.ch == ch }
	}
}