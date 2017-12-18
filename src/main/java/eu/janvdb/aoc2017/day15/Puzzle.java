package eu.janvdb.aoc2017.day15;

import java.util.stream.IntStream;

public class Puzzle {

    private static final int MULTIPLIER_A = 16807;
    private static final int MULTIPLIER_B = 48271;
//    private static final int SEED_A = 65;
//    private static final int SEED_B = 8921;
    private static final int SEED_A = 634;
    private static final int SEED_B = 301;
    private static final int NUMBER_TO_CONSIDER_1 = 40_000_000;
    private static final int NUMBER_TO_CONSIDER_2 = 5_000_000;

    public static void main(String[] args) {
        printMatchingPairs(1, 1, NUMBER_TO_CONSIDER_1);
        printMatchingPairs(4, 8, NUMBER_TO_CONSIDER_2);
    }

    private static void printMatchingPairs(int commonDenominatorA, int commonDenominatorB, int numberToConsider) {
        Generator generatorA = new Generator(MULTIPLIER_A, SEED_A, commonDenominatorA);
        Generator generatorB = new Generator(MULTIPLIER_B, SEED_B, commonDenominatorB);

        long matchingPairs = IntStream.range(0, numberToConsider)
                .filter(i -> generatorA.getNextValueLowestBits() == generatorB.getNextValueLowestBits())
                .count();

        System.out.println(matchingPairs);
    }
}
