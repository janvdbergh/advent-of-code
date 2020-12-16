package eu.janvdb.aoc2020.day16

val FIELD_REGEX = Regex("([a-z ]+): (\\d+)-(\\d+) or (\\d+)-(\\d+)")

data class TicketField(val name: String, val ranges: List<IntRange>) {
	fun matches(value: Int): Boolean {
		return ranges.any { it.contains(value) }
	}

	companion object {
		fun read(line: String): TicketField {
			val matchResult = FIELD_REGEX.matchEntire(line) ?: throw IllegalArgumentException(line)
			return TicketField(
				matchResult.groupValues[1], listOf(
					IntRange(matchResult.groupValues[2].toInt(), matchResult.groupValues[3].toInt()),
					IntRange(matchResult.groupValues[4].toInt(), matchResult.groupValues[5].toInt()),
				)
			)
		}
	}
}