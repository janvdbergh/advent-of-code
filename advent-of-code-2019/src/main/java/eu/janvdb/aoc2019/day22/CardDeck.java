package eu.janvdb.aoc2019.day22;

import java.math.BigInteger;

public class CardDeck implements Permutable<CardDeck> {

	private final int[] cards;
	private final int numberOfCards;

	public CardDeck(int numberOfCards) {
		this.cards = new int[numberOfCards];
		for (int i = 0; i < numberOfCards; i++) {
			cards[i] = i;
		}

		this.numberOfCards = numberOfCards;
	}

	private CardDeck(int[] cards) {
		this.cards = cards;
		this.numberOfCards = cards.length;
	}

	@Override
	public CardDeck dealIntoNewStack() {
		int[] newCards = new int[numberOfCards];
		for (int i = 0; i < numberOfCards; i++) {
			newCards[i] = cards[numberOfCards - i - 1];
		}

		return new CardDeck(newCards);
	}

	@Override
	public CardDeck cut(BigInteger cut) {
		int myNumberToCut = cut.intValue() % numberOfCards;
		if (myNumberToCut < 0) myNumberToCut += numberOfCards;

		int[] newCards = new int[numberOfCards];
		System.arraycopy(cards, 0, newCards, numberOfCards - myNumberToCut, myNumberToCut);
		System.arraycopy(cards, myNumberToCut, newCards, 0, numberOfCards - myNumberToCut);
		return new CardDeck(newCards);
	}

	@Override
	public CardDeck dealWithIncrement(BigInteger increment) {
		int[] newCards = new int[numberOfCards];
		int position = 0;
		for (int i = 0; i < numberOfCards; i++) {
			newCards[position] = cards[i];
			position = (position + increment.intValue()) % numberOfCards;
			if (position < 0) position += numberOfCards;
		}

		return new CardDeck(newCards);
	}

	public int[] getCards() {
		return cards;
	}

	public int findPositionOf(int card) {
		for (int i = 0; i < numberOfCards; i++) {
			if (cards[i] == card) return i;
		}
		throw new IllegalArgumentException();
	}
}
