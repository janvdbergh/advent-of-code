package eu.janvdb.aoc2022.day11

data class Monkey(
	val startItems: List<Long>, val operation: (Long) -> Long, val divisibleBy: Long,
	val throwToTrue: Int, val throwToFalse: Int
) {
	var itemsInvestigated = 0L
	val items = startItems.toMutableList()

	fun doRound() {
		items.forEach { level ->
			val newLevel = operation.invoke(level) / DIVISOR % MONKEYS.moduloFactor
			if (newLevel % divisibleBy == 0L) {
				MONKEYS.throwTo(throwToTrue, newLevel)
			} else {
				MONKEYS.throwTo(throwToFalse, newLevel)
			}
		}
		itemsInvestigated += items.size
		items.clear()
	}
}