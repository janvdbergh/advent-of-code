package eu.janvdb.aoc2020.common

object MatchFinder {

	/**
	 * Finds a unique matching value for each key, so that the match function is true.
	 * This assumes there is only one assignment.
	 */
	fun <KeyType, ValueType> findMatch(keys: List<KeyType>, values: List<ValueType>,
									   matchFunction: (key: KeyType, value: ValueType) -> Boolean): Map<KeyType, ValueType> {
		val possibleValues = keys.asSequence().map { key ->
			Pair(key, values.filter { value -> matchFunction.invoke(key, value) })
		}.toMap()

		return findMatch(possibleValues)
	}

	/**
	 * Finds a unique matching value for each key.
	 * This assumes there is only one assignment.
	 */
	fun <KeyType, ValueType> findMatch(possibleValues: Map<KeyType, List<ValueType>>): Map<KeyType, ValueType> {
		val remainingValues: MutableMap<KeyType, MutableList<ValueType>> = possibleValues.map { Pair(it.key, it.value.toMutableList()) }.toMap().toMutableMap()
		val valuesToAssign = possibleValues.values.asSequence().flatMap { it }.toMutableSet()
		val assignedValues = mutableMapOf<KeyType, ValueType>()

		// FIXME implement a smarter solution that can try solutions and backtrack
		var toAssignCount = valuesToAssign.size
		while (toAssignCount != 0) {
			remainingValues.filterValues { it.size == 1 }.forEach { key, values ->
				val value = values[0]
				assignedValues[key] = value
				valuesToAssign.remove(value)
				remainingValues.values.forEach { list -> list.remove(value) }
			}
			val newCount = valuesToAssign.size
			if (newCount == toAssignCount) throw RuntimeException("No solution found!")
			toAssignCount = newCount
		}

		return assignedValues
	}
}