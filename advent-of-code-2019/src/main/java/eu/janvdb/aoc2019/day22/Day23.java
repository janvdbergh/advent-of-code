package eu.janvdb.aoc2019.day22;

import eu.janvdb.aocutil.java.Holder;
import eu.janvdb.aocutil.java.InputReader;
import io.vavr.collection.List;
import io.vavr.collection.Seq;

import java.math.BigInteger;

public class Day23 {

	private static final BigInteger NUMBER_OF_CARDS_PART1 = BigInteger.valueOf(10007);
	private static final BigInteger NUMBER_OF_CARDS_PART2 = BigInteger.valueOf(119315717514047L);
	private static final BigInteger PERMUTATIONS_PART_2 = BigInteger.valueOf(101741582076661L);

	private static final String STEP_DEAL_WITH_INCREMENT = "deal with increment ";
	private static final String STEP_CUT = "cut ";
	private static final String STEP_DEAL_INTO_NEW_STACK = "deal into new stack";

	public static void main(String[] args) {
		new Day23().run();
	}

	private void run() {
		List<String> steps = InputReader.readInput(Day23.class.getResource("input.txt")).toList();

		part1(steps);
		part2(steps);
	}

	private void part1(List<String> steps) {
		Permutation permutation = getCombinedPermutation(steps, NUMBER_OF_CARDS_PART1);
		int result = permutation.apply(new CardDeck(NUMBER_OF_CARDS_PART1.intValue())).findPositionOf(2019);
		System.out.println(result);
	}

	private void part2(List<String> steps) {
		Permutation permutation = getCombinedPermutation(steps, NUMBER_OF_CARDS_PART2).toPower(PERMUTATIONS_PART_2);

		System.out.println(permutation.getValueAtPosition(BigInteger.valueOf(2020))); //>1504531 //<17637266442592
	}

	private Permutation getCombinedPermutation(Seq<String> steps, BigInteger numberOfCards) {
		Holder<Permutation> permutationHolder = new Holder<>(new Permutation(numberOfCards));
		steps.forEach(step -> permutationHolder.setValue(executeStep(step, permutationHolder.getValue())));
		return permutationHolder.getValue();
	}

	private <T extends Permutable<T>> T executeStep(String step, T permutable) {
		if (step.startsWith(STEP_DEAL_WITH_INCREMENT)) {
			long increment = Long.parseLong(step.substring(STEP_DEAL_WITH_INCREMENT.length()));
			return permutable.dealWithIncrement(BigInteger.valueOf(increment));
		} else if (step.startsWith(STEP_CUT)) {
			long cut = Long.parseLong(step.substring(STEP_CUT.length()));
			return permutable.cut(BigInteger.valueOf(cut));
		} else if (step.equals(STEP_DEAL_INTO_NEW_STACK)) {
			return permutable.dealIntoNewStack();
		} else {
			throw new IllegalArgumentException(step);
		}
	}
}
