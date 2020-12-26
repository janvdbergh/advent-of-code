package eu.janvdb.aoc2017.day15;

class Generator {

    private static final int MAX_VALUE = 2147483647;

    private final int multiplier;
    private final int commonDenominator;
    private long nextValue;

    Generator(int multiplier, int seed, int commonDenominator) {
        this.multiplier = multiplier;
        this.nextValue = seed;
        this.commonDenominator = commonDenominator;
    }

    int getNextValueLowestBits() {
        do {
            nextValue = nextValue * multiplier % MAX_VALUE;
        } while (nextValue % commonDenominator != 0);

        return (int) nextValue % 65536;
    }
}
