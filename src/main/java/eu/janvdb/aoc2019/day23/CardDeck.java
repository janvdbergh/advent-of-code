package eu.janvdb.aoc2019.day23;

public class CardDeck implements Permutable<CardDeck> {

	private final int[] cards;
	private int numberOfCards;

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
	public CardDeck cut(int numberToCut) {
		numberToCut = numberToCut % numberOfCards;
		if (numberToCut<0) numberToCut += numberOfCards;

		int[] newCards = new int[numberOfCards];
		System.arraycopy(cards, 0, newCards, numberOfCards - numberToCut, numberToCut);
		System.arraycopy(cards, numberToCut, newCards, 0, numberOfCards - numberToCut);
		return new CardDeck(newCards);
	}

	@Override
	public CardDeck dealWithIncrement(int increment) {
		int[] newCards = new int[numberOfCards];
		int position = 0;
		for(int i=0; i<numberOfCards; i++) {
			newCards[position] = cards[i];
			position = (position + increment) % numberOfCards;
			if (position<0) position += numberOfCards;
		}

		return new CardDeck(newCards);
	}

	public int[] getCards() {
		return cards;
	}

	public int findPositionOf(int card) {
		for(int i=0; i<numberOfCards; i++) {
			if (cards[i]==card) return i;
		}
		throw new IllegalArgumentException();
	}

	public void print() {
		for(int i=0; i<numberOfCards; i++) {
			if (i!=0) System.out.print(", ");
			System.out.print(cards[i]);
		}
		System.out.println();
	}
}
