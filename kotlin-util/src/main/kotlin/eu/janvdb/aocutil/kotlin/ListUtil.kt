package eu.janvdb.aocutil.kotlin

fun <T> List<T>.findRepeatingPattern(): Repetition? {
	fun checkRepetition(length: Int, offset: Int): Boolean {
		for (x in 0 until length) {
			for (y in offset + x until this.size - length step length) {
				if (this[y] != this[y + length]) {
					return false
				}
			}
		}

		return true
	}

	for (length in 1 until this.size / 2) {
		for (offset in 0 until length) {
			if (checkRepetition(length, offset)) {
				return Repetition(offset, length)
			}
		}
	}

	return null
}

data class Repetition(val offset: Int, val length: Int)