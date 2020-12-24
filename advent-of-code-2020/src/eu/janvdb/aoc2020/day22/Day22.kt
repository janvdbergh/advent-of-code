package eu.janvdb.aoc2020.day22

import eu.janvdb.aoc2020.common.readGroupedLines

fun main() {
	val decks = readGroupedLines("input22.txt").map(::parseDeck)
	val game = Game(decks[0], decks[1])
	part1(game)
	part2(game)
}

fun part1(game: Game) {
	val winner = playWithRules1(game)
	println(winner.deck.score().toString())
}

fun part2(game: Game) {
	val winner = playWithRules2(game)
	println(winner.deck.score().toString())
}

fun parseDeck(lines: List<String>): Deck {
	val cards = lines.drop(1).map(String::toInt).toMutableList()
	return Deck(cards)
}

fun playWithRules1(game: Game): Winner {
	var currentGame = game

	while(true) {
		val deck0 = currentGame.deck0
		val deck1 = currentGame.deck1

		if (deck1.isEmpty()) return Winner(0, deck0)
		if (deck0.isEmpty()) return Winner(1, deck1)

		val card0 = deck0.takeOne()
		val card1 = deck1.takeOne()
		currentGame = if (card0 > card1) {
			Game(deck0.replaceFirst(card0, card1), deck1.replaceFirst())
		} else {
			Game(deck0.replaceFirst(), deck1.replaceFirst(card1, card0))
		}
	}
}

fun playWithRules2(game: Game, indent: String = ""): Winner {
	val gamesPlayed = mutableSetOf<Game>()
	var currentGame = game
	var round = 1

	while(true) {
		val deck0 = currentGame.deck0
		val deck1 = currentGame.deck1

		println("$indent($round) $currentGame")
		round++

		if (gamesPlayed.contains(currentGame)) {
			println("$indent\tAlready played -> Winner 0")
			return Winner(0, deck0)
		}
		gamesPlayed.add(currentGame)

		if (currentGame.deck1.isEmpty()) return Winner(0, deck0)
		if (deck0.isEmpty()) return Winner(1, currentGame.deck1)

		val card0 = deck0.takeOne()
		val card1 = currentGame.deck1.takeOne()

		val winner = if (deck0.size - 1 >= card0 && currentGame.deck1.size - 1 >= card1) {
			val newDeck0 = deck0.subDeck(1, card0)
			val newDeck1 = deck1.subDeck(1, card1)
			val newGame = Game(newDeck0, newDeck1)
			playWithRules2(newGame, indent + "\t").player
		} else {
			if (card0 > card1) 0 else 1
		}

		println("$indent\tWinner $winner")

		currentGame = if (winner == 0) {
			Game(deck0.replaceFirst(card0, card1), deck1.replaceFirst())
		} else {
			Game(deck0.replaceFirst(), deck1.replaceFirst(card1, card0))
		}
	}
}

data class Deck(val cards: List<Int>) {
	val size = cards.size
	fun isEmpty() = cards.isEmpty()
	fun takeOne(): Int = cards[0]

	fun replaceFirst(vararg cardsToAdd: Int): Deck {
		val newCards = cards.toMutableList()
		newCards.removeAt(0)
		cardsToAdd.forEach(newCards::add)
		return Deck(newCards)
	}

	fun subDeck(start: Int, numberOfCards: Int): Deck = Deck(cards.subList(start, start + numberOfCards))

	fun score(): Int {
		return (cards.indices).map { (cards.size - it) * cards[it] }.sum()
	}
}

data class Game(val deck0: Deck, val deck1: Deck)

data class Winner(val player: Int, val deck: Deck)