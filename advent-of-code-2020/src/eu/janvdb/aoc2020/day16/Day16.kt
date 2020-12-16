package eu.janvdb.aoc2020.day16

import eu.janvdb.aoc2020.common.MatchFinder
import eu.janvdb.aoc2020.common.readGroupedLines


fun main() {
	val (fields, tickets) = readInput()
	part1(tickets, fields)
	part2(tickets, fields)
}

private fun readInput(): Pair<List<TicketField>, List<Ticket>> {
	val groupedLines = readGroupedLines("input16.txt")
	assert(groupedLines.size == 3)
	assert(groupedLines[1][0] == "your ticket:")
	assert(groupedLines[2][0] == "nearby tickets:")

	val fields = groupedLines[0].map(TicketField::read)

	val myTickets = groupedLines[1].drop(1).map(Ticket::read)
	val otherTickets = groupedLines[2].drop(1).map(Ticket::read)
	val allTickets = myTickets + otherTickets
	return Pair(fields, allTickets)
}

private fun part1(allTickets: List<Ticket>, fields: List<TicketField>) {
	val invalidValues = allTickets.flatMap { it.getInvalidValues(fields) }
	val invalidSum = invalidValues.sum()
	println(invalidSum)
}

private fun part2(tickets: List<Ticket>, fields: List<TicketField>) {
	val validTickets = tickets.filter { it.hasInvalidValues(fields) }

	val indices = IntRange(0, fields.size - 1).toList()
	val match = MatchFinder.findMatch(fields, indices) { field, index ->
		validTickets.all { ticket -> field.matches(ticket.values[index]) }
	}

	println(match)

	val myTicket = tickets[0]
	val result = match.filterKeys { it.name.startsWith("departure") }
		.map { myTicket.values[it.value] }
		.foldRight(1L, { x, y -> x * y })
	println(result)
}

