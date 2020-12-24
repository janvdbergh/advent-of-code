package eu.janvdb.aoc2020.common

object MatchFinder {

	/**
	 * Finds a unique matching value for each key, so that the match function is true.
	 * This assumes there is only one assignment.
	 */
	fun <KeyType, ValueType> findMatch(
		keys: List<KeyType>, values: List<ValueType>,
		matchFunction: (key: KeyType, value: ValueType) -> Boolean
	): Map<KeyType, ValueType> {
		val possibleValues = keys.asSequence().map { key ->
			Pair(key, values.filter { value -> matchFunction.invoke(key, value) })
		}.toMap()

		return findMatch(keys, setOf(), possibleValues)!!
	}

	/**
	 * Finds a unique matching value for each key, so that the match function is true.
	 * This assumes there is only one assignment.
	 */
	private fun <KeyType, ValueType> findMatch(
		keys: List<KeyType>,
		assignedValues: Set<ValueType>,
		possibleValues: Map<KeyType, List<ValueType>>
	): Map<KeyType, ValueType>? {
		if (keys.isEmpty()) return mapOf()

		val key = keys.minByOrNull { possibleValues[it]!!.minus(assignedValues).size }!!
		val remainingKeys = keys.minus(key)

		for (value in possibleValues[key]!!.minus(assignedValues)) {
			val match = findMatch(remainingKeys, assignedValues + value, possibleValues)
			if (match != null) {
				return match.plus(Pair(key, value))
			}
		}

		return null
	}
}