package eu.janvdb.aoc2019.day23;

import eu.janvdb.util.Holder;
import eu.janvdb.util.InputReader;
import io.vavr.collection.List;

public class Day23 {

	public static final int NUMBER_OF_CARDS = 10007;
	public static final String STEP_DEAL_WITH_INCREMENT = "deal with increment ";
	public static final String STEP_CUT = "cut ";
	public static final String STEP_DEAL_INTO_NEW_STACK = "deal into new stack";

	public static void main(String[] args) {
		new Day23().run();
	}

	private void run() {
		List<String> steps = InputReader.readInput(Day23.class.getResource("input.txt")).toList();
		part1UsingCardDeck(steps);
		part1UsingPermutation(steps);
	}

	private void part1UsingCardDeck(List<String> steps) {
		Holder<CardDeck> cardDeckHolder = new Holder<>(new CardDeck(NUMBER_OF_CARDS));
		steps.forEach(step -> cardDeckHolder.setValue(executeStep(step, cardDeckHolder.getValue())));

		int result = cardDeckHolder.getValue().findPositionOf(2019);
		System.out.println(result);
	}

	private void part1UsingPermutation(List<String> steps) {
		Holder<Permutation> permutationHolder = new Holder<>(new Permutation(NUMBER_OF_CARDS));
		steps.forEach(step -> permutationHolder.setValue(executeStep(step, permutationHolder.getValue())));

		int result = permutationHolder.getValue().apply(new CardDeck(NUMBER_OF_CARDS)).findPositionOf(2019);
		System.out.println(result);
	}

	private <T extends Permutable<T>> T executeStep(String step, T permutable) {
		if (step.startsWith(STEP_DEAL_WITH_INCREMENT)) {
			int increment = Integer.parseInt(step.substring(STEP_DEAL_WITH_INCREMENT.length()));
			return permutable.dealWithIncrement(increment);
		} else if (step.startsWith(STEP_CUT)) {
			int cut = Integer.parseInt(step.substring(STEP_CUT.length()));
			return permutable.cut(cut);
		} else if (step.equals(STEP_DEAL_INTO_NEW_STACK)) {
			return permutable.dealIntoNewStack();
		} else {
			throw new IllegalArgumentException(step);
		}
	}
}
