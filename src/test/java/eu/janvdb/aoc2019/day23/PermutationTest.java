package eu.janvdb.aoc2019.day23;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PermutationTest {

	public static final int NUMBER_OF_CARDS = 10;

	@Test
	public void dealIntoNewStackTest() {
		CardDeck cardDeck = new CardDeck(NUMBER_OF_CARDS);
		CardDeck newCardDeck = new Permutation(NUMBER_OF_CARDS).dealIntoNewStack().apply(cardDeck);
		assertThat(newCardDeck.getCards()).containsExactly(9, 8, 7, 6, 5, 4, 3, 2, 1, 0);
	}

	@Test
	public void cutTest() {
		CardDeck cardDeck = new CardDeck(10);
		CardDeck newCardDeck = new Permutation(NUMBER_OF_CARDS).cut(3).apply(cardDeck);
		assertThat(newCardDeck.getCards()).containsExactly(3, 4, 5, 6, 7, 8, 9, 0, 1, 2);
	}

	@Test
	public void cut0Test() {
		CardDeck cardDeck = new CardDeck(10);
		CardDeck newCardDeck = new Permutation(NUMBER_OF_CARDS).cut(0).apply(cardDeck);
		assertThat(newCardDeck.getCards()).containsExactly(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
	}

	@Test
	public void cut10Test() {
		CardDeck cardDeck = new CardDeck(10);
		CardDeck newCardDeck = new Permutation(NUMBER_OF_CARDS).cut(10).apply(cardDeck);
		assertThat(newCardDeck.getCards()).containsExactly(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
	}

	@Test
	public void cutNegativeTest() {
		CardDeck cardDeck = new CardDeck(10);
		CardDeck newCardDeck = new Permutation(NUMBER_OF_CARDS).cut(-4).apply(cardDeck);
		assertThat(newCardDeck.getCards()).containsExactly(6, 7, 8, 9, 0, 1, 2, 3, 4, 5);
	}

	@Test
	public void dealWithIncrement() {
		CardDeck cardDeck = new CardDeck(10);
		CardDeck newCardDeck = new Permutation(NUMBER_OF_CARDS).dealWithIncrement(3).apply(cardDeck);
		assertThat(newCardDeck.getCards()).containsExactly(0, 7, 4, 1, 8, 5, 2, 9, 6, 3);
	}

	@Test
	public void dealWithNegativeIncrement() {
		CardDeck cardDeck = new CardDeck(10);
		CardDeck newCardDeck = new Permutation(NUMBER_OF_CARDS).dealWithIncrement(-1).apply(cardDeck);
		assertThat(newCardDeck.getCards()).containsExactly(0, 9, 8, 7, 6, 5, 4, 3, 2, 1);
	}

	@Test
	public void combinationTest() {
		CardDeck cardDeck = new CardDeck(10);
		CardDeck newCardDeck = new Permutation(NUMBER_OF_CARDS)
				.dealIntoNewStack()
				.cut(-2)
				.dealWithIncrement(7)
				.cut(8)
				.cut(-4)
				.dealWithIncrement(7)
				.cut(3)
				.dealWithIncrement(9)
				.dealWithIncrement(3)
				.cut(-1)
				.apply(cardDeck);
		assertThat(newCardDeck.getCards()).containsExactly(9, 2, 5, 8, 1, 4, 7, 0, 3, 6);
	}
}