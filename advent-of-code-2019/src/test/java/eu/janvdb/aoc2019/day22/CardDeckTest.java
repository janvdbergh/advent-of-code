package eu.janvdb.aoc2019.day22;

import org.junit.jupiter.api.Test;

import static java.math.BigInteger.valueOf;
import static org.assertj.core.api.Assertions.assertThat;

class CardDeckTest {

	@Test
	public void dealIntoNewStackTest() {
		CardDeck cardDeck = new CardDeck(10);
		CardDeck newCardDeck = cardDeck.dealIntoNewStack();
		assertThat(newCardDeck.getCards()).containsExactly(9, 8, 7, 6, 5, 4, 3, 2, 1, 0);
	}

	@Test
	public void cutTest() {
		CardDeck cardDeck = new CardDeck(10);
		CardDeck newCardDeck = cardDeck.cut(valueOf(3));
		assertThat(newCardDeck.getCards()).containsExactly(3, 4, 5, 6, 7, 8, 9, 0, 1, 2);
	}

	@Test
	public void cut0Test() {
		CardDeck cardDeck = new CardDeck(10);
		CardDeck newCardDeck = cardDeck.cut(valueOf(0));
		assertThat(newCardDeck.getCards()).containsExactly(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
	}

	@Test
	public void cut10Test() {
		CardDeck cardDeck = new CardDeck(10);
		CardDeck newCardDeck = cardDeck.cut(valueOf(10));
		assertThat(newCardDeck.getCards()).containsExactly(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
	}

	@Test
	public void cutNegativeTest() {
		CardDeck cardDeck = new CardDeck(10);
		CardDeck newCardDeck = cardDeck.cut(valueOf(-4));
		assertThat(newCardDeck.getCards()).containsExactly(6, 7, 8, 9, 0, 1, 2, 3, 4, 5);
	}

	@Test
	public void dealWithIncrement() {
		CardDeck cardDeck = new CardDeck(10);
		CardDeck newCardDeck = cardDeck.dealWithIncrement(valueOf(3));
		assertThat(newCardDeck.getCards()).containsExactly(0, 7, 4, 1, 8, 5, 2, 9, 6, 3);
	}

	@Test
	public void dealWithNegativeIncrement() {
		CardDeck cardDeck = new CardDeck(10);
		CardDeck newCardDeck = cardDeck.dealWithIncrement(valueOf(-1));
		assertThat(newCardDeck.getCards()).containsExactly(0, 9, 8, 7, 6, 5, 4, 3, 2, 1);
	}

	@Test
	public void combinationTest() {
		CardDeck newCardDeck = new CardDeck(10)
				.dealIntoNewStack()
				.cut(valueOf(-2))
				.dealWithIncrement(valueOf(7))
				.cut(valueOf(8))
				.cut(valueOf(-4))
				.dealWithIncrement(valueOf(7))
				.cut(valueOf(3))
				.dealWithIncrement(valueOf(9))
				.dealWithIncrement(valueOf(3))
				.cut(valueOf(-1));
		assertThat(newCardDeck.getCards()).containsExactly(9, 2, 5, 8, 1, 4, 7, 0, 3, 6);
	}
}