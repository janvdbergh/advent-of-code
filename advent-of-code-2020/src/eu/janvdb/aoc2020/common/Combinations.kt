package eu.janvdb.aoc2020.common

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
