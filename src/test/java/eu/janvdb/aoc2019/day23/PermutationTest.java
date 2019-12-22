package eu.janvdb.aoc2019.day23;

import io.vavr.collection.Stream;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static java.math.BigInteger.ZERO;
import static java.math.BigInteger.valueOf;
import static org.assertj.core.api.Assertions.assertThat;

class PermutationTest {

	public static final BigInteger NUMBER_OF_CARDS = BigInteger.TEN;

	@Test
	public void dealIntoNewStackTest() {
		CardDeck cardDeck = new CardDeck(NUMBER_OF_CARDS.intValue());
		CardDeck newCardDeck = new Permutation(NUMBER_OF_CARDS).dealIntoNewStack().apply(cardDeck);
		assertThat(newCardDeck.getCards()).containsExactly(9, 8, 7, 6, 5, 4, 3, 2, 1, 0);
	}

	@Test
	public void cutTest() {
		CardDeck cardDeck = new CardDeck(NUMBER_OF_CARDS.intValue());
		CardDeck newCardDeck = new Permutation(NUMBER_OF_CARDS).cut(valueOf(3)).apply(cardDeck);
		assertThat(newCardDeck.getCards()).containsExactly(3, 4, 5, 6, 7, 8, 9, 0, 1, 2);
	}

	@Test
	public void cut0Test() {
		CardDeck cardDeck = new CardDeck(NUMBER_OF_CARDS.intValue());
		CardDeck newCardDeck = new Permutation(NUMBER_OF_CARDS).cut(ZERO).apply(cardDeck);
		assertThat(newCardDeck.getCards()).containsExactly(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
	}

	@Test
	public void cut10Test() {
		CardDeck cardDeck = new CardDeck(NUMBER_OF_CARDS.intValue());
		CardDeck newCardDeck = new Permutation(NUMBER_OF_CARDS).cut(NUMBER_OF_CARDS).apply(cardDeck);
		assertThat(newCardDeck.getCards()).containsExactly(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
	}

	@Test
	public void cutNegativeTest() {
		CardDeck cardDeck = new CardDeck(NUMBER_OF_CARDS.intValue());
		CardDeck newCardDeck = new Permutation(NUMBER_OF_CARDS).cut(valueOf(-4)).apply(cardDeck);
		assertThat(newCardDeck.getCards()).containsExactly(6, 7, 8, 9, 0, 1, 2, 3, 4, 5);
	}

	@Test
	public void dealWithIncrement() {
		CardDeck cardDeck = new CardDeck(NUMBER_OF_CARDS.intValue());
		CardDeck newCardDeck = new Permutation(NUMBER_OF_CARDS).dealWithIncrement(valueOf(3)).apply(cardDeck);
		assertThat(newCardDeck.getCards()).containsExactly(0, 7, 4, 1, 8, 5, 2, 9, 6, 3);
	}

	@Test
	public void dealWithNegativeIncrement() {
		CardDeck cardDeck = new CardDeck(NUMBER_OF_CARDS.intValue());
		CardDeck newCardDeck = new Permutation(NUMBER_OF_CARDS).dealWithIncrement(valueOf(-1)).apply(cardDeck);
		assertThat(newCardDeck.getCards()).containsExactly(0, 9, 8, 7, 6, 5, 4, 3, 2, 1);
	}

	@Test
	public void combinationTest() {
		CardDeck cardDeck = new CardDeck(NUMBER_OF_CARDS.intValue());
		CardDeck newCardDeck = new Permutation(NUMBER_OF_CARDS)
				.dealIntoNewStack()
				.cut(valueOf(-2))
				.dealWithIncrement(valueOf(7))
				.cut(valueOf(8))
				.cut(valueOf(-4))
				.dealWithIncrement(valueOf(7))
				.cut(valueOf(3))
				.dealWithIncrement(valueOf(9))
				.dealWithIncrement(valueOf(3))
				.cut(valueOf(-1))
				.apply(cardDeck);
		assertThat(newCardDeck.getCards()).containsExactly(9, 2, 5, 8, 1, 4, 7, 0, 3, 6);
	}

	@Test
	void andThenTest() {
		Permutation permutation1 = new Permutation(NUMBER_OF_CARDS).cut(valueOf(3)).dealWithIncrement(valueOf(2));
		Permutation permutation2 = new Permutation(NUMBER_OF_CARDS).cut(valueOf(4)).dealWithIncrement(valueOf(5));
		assertThat(permutation1.andThen(permutation2)).isEqualTo(permutation1.cut(valueOf(4)).dealWithIncrement(valueOf(5)));
	}

	@Test
	public void powerTest() {
		Permutation permutation = new Permutation(NUMBER_OF_CARDS).cut(valueOf(3)).dealWithIncrement(valueOf(2));

		assertThat(permutation.toPower(valueOf(0))).isEqualTo(new Permutation(NUMBER_OF_CARDS));
		assertThat(permutation.toPower(valueOf(1))).isEqualTo(permutation);
		assertThat(permutation.toPower(valueOf(2))).isEqualTo(permutation.square());
		assertThat(permutation.toPower(valueOf(5))).isEqualTo(permutation.andThen(permutation.square().square()));
	}

	@Test
	public void valueOfCardTest() {
		testWithPermutation(new Permutation(NUMBER_OF_CARDS));
		testWithPermutation(new Permutation(NUMBER_OF_CARDS).cut(valueOf(2)));
		testWithPermutation(new Permutation(NUMBER_OF_CARDS).dealWithIncrement(valueOf(3)));
		testWithPermutation(new Permutation(NUMBER_OF_CARDS).dealWithIncrement(valueOf(3)).cut(valueOf(2)));
		testWithPermutation(new Permutation(NUMBER_OF_CARDS).dealWithIncrement(valueOf(-1)).cut(valueOf(-5)));
	}

	private void testWithPermutation(Permutation permutation) {
		CardDeck transformedCardDeck = permutation.apply(new CardDeck(NUMBER_OF_CARDS.intValue()));

		int[] permutedValues = Stream.range(0, NUMBER_OF_CARDS.intValue())
				.map(BigInteger::valueOf)
				.map(permutation::getValueAtPosition)
				.toJavaStream().mapToInt(BigInteger::intValue).toArray();
		assertThat(permutedValues).isEqualTo(transformedCardDeck.getCards());
	}
}