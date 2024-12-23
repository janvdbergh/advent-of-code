package eu.janvdb.aocutil.kotlin

object Combinations {

	fun iterate(minMaxes: List<MinMax>): Sequence<List<Int>> {
		fun iterateIndices(currentIndex: Int): Sequence<Array<Int>> {
			val min = minMaxes[currentIndex].minInclusive
			val max = minMaxes[currentIndex].maxInclusive
			val intSequence = IntRange(min, max).asSequence()

			if (currentIndex == minMaxes.size - 1) {
				return intSequence.map { value -> Array(minMaxes.size) {value} }
			}

			return intSequence.flatMap { value ->
				iterateIndices(currentIndex + 1).map { values -> values[currentIndex] = value; values }
			}
		}

		return iterateIndices(0).map { it.asList()}
	}
}

data class MinMax(val minInclusive: Int, val maxInclusive: Int)

fun <T> findCombinations(elements: List<T>, numberOfItems: Int): Sequence<List<T>> {
	val indices = Array(numberOfItems) { it }

	fun nextValue() = indices.map { elements[it] }

	fun moveToNext(): Boolean {
		var current = numberOfItems - 1
		while (current >= 0 && ++indices[current] == elements.size - numberOfItems + current + 1)
			current--
		if (current == -1) return false
		for (i in current + 1 until numberOfItems) {
			indices[i] = indices[i - 1] + 1
		}
		return true
	}

	return sequence {
		do {
			yield(nextValue())
		} while (moveToNext())
	}
}