package eu.janvdb.aoc2022.day11

data class Monkeys(val monkeys: List<Monkey>) {
	val moduloFactor = monkeys.fold(1L) { acc, monkey -> acc * monkey.divisibleBy }

	fun throwTo(index: Int, item: Long) {
		monkeys[index].items += item
	}

	fun doRound() {
		monkeys.forEach(Monkey::doRound)
	}

	fun getScore(): Long {
		return monkeys.asSequence()
			.map(Monkey::itemsInvestigated)
			.sortedDescending()
			.take(2)
			.fold(1L) { x, y -> x * y }
	}
}