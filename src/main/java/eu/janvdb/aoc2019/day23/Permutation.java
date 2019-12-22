package eu.janvdb.aoc2019.day23;

public class Permutation implements Permutable<Permutation> {

	private int numberOfCards;
	private final int dealWithIncrement;
	private final int cut;

	public Permutation(int numberOfCards) {
		this(numberOfCards, 1, 0);
	}

	private Permutation(int numberOfCards, int dealWithIncrement, int cut) {
		this.numberOfCards = numberOfCards;
		this.dealWithIncrement = dealWithIncrement % numberOfCards;
		this.cut = cut % numberOfCards;
	}

	public CardDeck apply(CardDeck cardDeck) {
		return cardDeck.dealWithIncrement(dealWithIncrement).cut(cut);
	}

	public Permutation dealWithIncrement(int dealWithIncrement) {
		return new Permutation(
				this.numberOfCards,
				this.dealWithIncrement * dealWithIncrement,
				this.cut * dealWithIncrement
		);
	}

	public Permutation cut(int cut) {
		return new Permutation(
				numberOfCards,
				this.dealWithIncrement,
				this.cut + cut
		);
	}

	public Permutation dealIntoNewStack() {
		return this.dealWithIncrement(-1).cut(1);
	}
}
