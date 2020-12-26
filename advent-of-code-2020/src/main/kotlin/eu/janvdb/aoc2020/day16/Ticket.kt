package eu.janvdb.aoc2020.day16

data class Ticket(val values: List<Int>) {
	fun getInvalidValues(fields: List<TicketField>): List<Int> {
		return values.filter { value -> !fields.any { field -> field.matches(value) } }
	}

	fun hasInvalidValues(fields: List<TicketField>): Boolean {
		return values.none { value -> !fields.any { field -> field.matches(value) } }
	}

	companion object {
		fun read(line: String): Ticket {
			return Ticket(line.split(",").map(String::toInt))
		}
	}
}