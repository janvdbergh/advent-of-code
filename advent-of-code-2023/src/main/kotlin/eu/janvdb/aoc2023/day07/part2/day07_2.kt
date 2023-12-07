package eu.janvdb.aoc2023.day07.part2

import eu.janvdb.aoc2023.day07.TypeOfHand
import eu.janvdb.aocutil.kotlin.readLines

//const val FILENAME = "input07-test.txt"
const val FILENAME = "input07.txt"

fun main() {
	val pairs = readLines(2023, FILENAME).map { CardBidPair.parse(it) }

	val score = pairs
		.sortedBy { it.hand }
		.asSequence()
		.mapIndexed { index, it -> (index + 1L) * it.bid }
		.sum()

	println(score)
}

data class CardBidPair(val hand: Hand, val bid: Int) {

	companion object {
		fun parse(input: String): CardBidPair {
			val split = input.trim().split(" ")
			return CardBidPair(Hand.parse(split[0]), split[1].toInt())
		}
	}
}

enum class Card(val symbol: Char) {
	JOKER('J'),
	TWO('2'),
	THREE('3'),
	FOUR('4'),
	FIVE('5'),
	SIX('6'),
	SEVEN('7'),
	EIGHT('8'),
	NINE('9'),
	TEN('T'),
	QUEEN('Q'),
	KING('K'),
	ACE('A');

	companion object {
		fun findBySymbol(symbol: Char) =
			entries.find { it.symbol == symbol } ?: throw IllegalArgumentException("Unknown symbol: $symbol")
	}
}

data class Hand(val cards: List<Card>) : Comparable<Hand> {
	val numberOfJokers = cards.count() { it == Card.JOKER }
	val occurrences =
		cards.filter { it != Card.JOKER }.groupingBy { it }.eachCount().map { it.value }.sortedDescending()
	val type = calculateType()

	private fun calculateType(): TypeOfHand {
		if (occurrences.isEmpty()) return TypeOfHand.FIVE_OF_A_KIND // Five jokers

		if (occurrences[0] + numberOfJokers == 5) return TypeOfHand.FIVE_OF_A_KIND
		if (occurrences[0] + numberOfJokers == 4) return TypeOfHand.FOUR_OF_A_KIND
		if (occurrences[0] + numberOfJokers == 3 && occurrences[1] == 2) return TypeOfHand.FULL_HOUSE
		if (occurrences[0] + numberOfJokers == 3) return TypeOfHand.THREE_OF_A_KIND
		if (occurrences[0] + numberOfJokers == 2 && occurrences[1] == 2) return TypeOfHand.TWO_PAIRS
		if (occurrences[0] + numberOfJokers == 2) return TypeOfHand.ONE_PAIR
		return TypeOfHand.HIGH_CARD
	}

	override fun compareTo(other: Hand): Int {
		if (type != other.type) return type.compareTo(other.type)
		for (i in cards.indices) {
			val thisCard = cards[i]
			val otherCard = other.cards[i]
			if (thisCard != otherCard) return thisCard.compareTo(otherCard)
		}

		return 0
	}

	override fun toString(): String {
		return "Hand(cards=$cards, numberOfJokers=$numberOfJokers, occurrences=$occurrences, type=$type)"
	}

	companion object {
		fun parse(input: String): Hand {
			val cards = input.toCharArray().map { Card.findBySymbol(it) }
			return Hand(cards)
		}
	}
}