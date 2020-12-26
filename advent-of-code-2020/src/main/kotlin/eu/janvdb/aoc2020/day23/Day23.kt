package eu.janvdb.aoc2020.day23

const val CUPS_TO_MOVE = 3
const val NUMBER_OF_MOVES_1 = 100
const val NUMBER_OF_MOVES_2 = 10_000_000

//const val INITIAL_CUPS = "389125467"
const val INITIAL_CUPS = "398254716"

const val TOTAL_CUPS_2 = 1_000_000

fun main() {
	part1()
	part2()
}

private fun part1() {
	val cups = Cups(INITIAL_CUPS.length) { INITIAL_CUPS[it] - '0' }
	cups.move(NUMBER_OF_MOVES_1)
	println(cups.getResult1())
}

fun part2() {
	val cups = Cups(TOTAL_CUPS_2) { index -> if (index < INITIAL_CUPS.length) INITIAL_CUPS[index] - '0' else index + 1 }
	cups.move(NUMBER_OF_MOVES_2)
	println(cups.getResult2())
}

class Cups(size: Int, initializer: (Int) -> (Int)) {

	private var firstElement: Element
	private val valueToElement = mutableMapOf<Int, Element>()

	val size get() = valueToElement.size

	init {
		fun createElement(index: Int): Element {
			val value = initializer.invoke(index)
			val element = Element(value)
			valueToElement[value] = element
			return element
		}

		firstElement = createElement(0)
		var current = firstElement
		for (index in 1 until size) {
			current.next = createElement(index)
			current = current.next
		}
		current.next = firstElement
	}

	fun move(moves: Int) {
		repeat(moves) { move() }
	}

	private fun move() {
		fun getItemsToMove(): List<Element> {
			val result = mutableListOf<Element>()
			var current = firstElement.next
			for (i in 0 until CUPS_TO_MOVE) {
				result.add(current)
				current = current.next
			}
			return result
		}

		fun findDestination(excluded: Set<Int>): Element {
			var currentValue = firstElement.value - 1
			while (currentValue == 0 || currentValue in excluded) {
				if (currentValue == 0) currentValue = size else currentValue--
			}
			return valueToElement[currentValue]!!
		}

		val itemsToMove = getItemsToMove()
		val destination = findDestination(itemsToMove.map(Element::value).toSet())

		val previousNext = destination.next
		firstElement.next = itemsToMove[CUPS_TO_MOVE - 1].next
		destination.next = itemsToMove[0]
		itemsToMove[CUPS_TO_MOVE - 1].next = previousNext

		firstElement = firstElement.next
	}

	fun getResult1(): String = buildString {
		val element1 = valueToElement[1]!!
		var current = element1.next
		while (current != element1) {
			append(current.value)
			current = current.next
		}
	}

	fun getResult2(): Long {
		val element1 = valueToElement[1]!!
		return 1L * element1.next.value * element1.next.next.value
	}

	internal class Element(val value: Int) {
		lateinit var next: Element
	}
}