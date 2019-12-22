package eu.janvdb.aoc2019.day23;

import java.math.BigInteger;
import java.util.Objects;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.TWO;
import static java.math.BigInteger.ZERO;

public class Permutation implements Permutable<Permutation> {

	private final BigInteger numberOfCards;
	private final BigInteger increment;
	private final BigInteger cut;

	public Permutation(BigInteger numberOfCards) {
		this(numberOfCards, ONE, ZERO);
	}

	private Permutation(BigInteger numberOfCards, BigInteger increment, BigInteger cut) {
		this.numberOfCards = numberOfCards;
		this.increment = (increment.add(numberOfCards)).mod(numberOfCards);
		this.cut = (cut.add(numberOfCards)).mod(numberOfCards);
	}

	public CardDeck apply(CardDeck cardDeck) {
		return cardDeck.dealWithIncrement(increment).cut(cut);
	}

	@Override
	public Permutation dealWithIncrement(BigInteger increment) {
		return new Permutation(
				numberOfCards,
				this.increment.multiply(increment),
				this.cut.multiply(increment)
		);
	}

	@Override
	public Permutation cut(BigInteger cut) {
		return new Permutation(
				numberOfCards,
				increment,
				this.cut.add(cut)
		);
	}

	@Override
	public Permutation dealIntoNewStack() {
		return this.dealWithIncrement(BigInteger.valueOf(-1)).cut(ONE);
	}

	public Permutation andThen(Permutation permutation) {
		return new Permutation(
				numberOfCards,
				increment.multiply(permutation.increment),
				this.cut.multiply(permutation.increment).add(permutation.cut)
		);
	}

	public Permutation square() {
		return andThen(this);
	}

	public Permutation toPower(BigInteger power) {
		Permutation current = this;
		Permutation result = new Permutation(numberOfCards);
		while (!power.equals(ZERO)) {
			if (power.mod(TWO).equals(ONE)) {
				result = result.andThen(current);
			}
			power = power.divide(TWO);
			current = current.square();
		}
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Permutation that = (Permutation) o;
		return numberOfCards.equals(that.numberOfCards) &&
				increment.equals(that.increment) &&
				cut.equals(that.cut);
	}

	@Override
	public int hashCode() {
		return Objects.hash(numberOfCards, increment, cut);
	}

	@Override
	public String toString() {
		return "Permutation{" +
				"numberOfCards=" + numberOfCards +
				", dealWithIncrement=" + increment +
				", cut=" + cut +
				'}';
	}

	public BigInteger getValueAtPosition(BigInteger position) {
		return position.add(cut).add(numberOfCards).multiply(increment.modInverse(numberOfCards)).mod(numberOfCards);
	}
}
