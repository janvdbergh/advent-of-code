package eu.janvdb.aoc2019.day16;

import java.util.stream.Collectors;

import io.vavr.collection.List;
import io.vavr.collection.Stream;

public class Day16 {

	private static final int[] BASE_PATTERN = {0, 1, 0, -1};
	private static final String INPUT = "597509395456041704904488069040539960193347671996345499088347757214057395968619" +
			"5264625497948318447116203629239042079402706436395488514756086791360588248962248704847905539627272415930146" +
			"4058399346811328233322326527416513041769256881220146486963575598109803656565965629866620042497176335792972" +
			"2125529856666205661673421402281231081314195657386622031883420872020648944100356967404181747102128516547222" +
			"7453333252548952701015287582273065994696240356807440825321888054771592149180313327240302753388690398226804" +
			"0703808320401476923037465500423410637688454817997420944672193747192363987753459196311580461975618629750912" +
			"028908140713295213305315022251918307904937";
	private static final int NUMBER_OF_ROUNDS = 100;
	private static final int REQUIRED_DIGITS = 8;
	private static final int MULTIPLIER = 10000;
	private static final int OFFSET_LENGTH = 7;

	public static void main(String[] args) {
		new Day16().run();
	}

	private void run() {
		part1();
		part2();
	}

	private void part1() {
		List<Integer> values = Stream.ofAll(Day16.INPUT.toCharArray()).map(ch -> ch - '0').toList();

		for (int i = 0; i < Day16.NUMBER_OF_ROUNDS; i++) {
			values = doRound(values);
			print(i, values);
		}
	}

	private List<Integer> doRound(List<Integer> values) {
		return Stream.range(0, values.length())
				.map(index -> Math.abs(calculateSumProduct(values, index) % 10))
				.toList();
	}

	private int calculateSumProduct(List<Integer> values, int index) {
		return values.toStream()
				.zipWithIndex()
				.map(tuple -> {
					int value = tuple._1;
					int index2 = tuple._2;
					int multiplier = getMultiplier(index, index2);
					return value * multiplier;
				})
				.sum().intValue();
	}

	private int getMultiplier(int index1, int index2) {
		// each digit pattern is repeated index1 times and skipped index2+1 times
		int offset = (index2 + 1) / (index1 + 1) % BASE_PATTERN.length;
		while (offset < 0) offset += BASE_PATTERN.length;
		return BASE_PATTERN[offset];
	}

	private void print(int round, List<Integer> values) {
		System.out.printf("After round %d: %s...\n",
				round,
				values.map(String::valueOf).collect(Collectors.joining()).substring(0, REQUIRED_DIGITS));
	}

	private void part2() {
		int totalLength = Day16.INPUT.length() * MULTIPLIER;
		int offset = Integer.parseInt(Day16.INPUT.substring(0, OFFSET_LENGTH));
		if (offset <= totalLength / 2) throw new IllegalArgumentException("Optimization does not work!");

		int arrayLength = totalLength - offset + 1;
		int[] values = new int[arrayLength];
		for (int i = offset; i <= totalLength; i++) {
			values[i - offset] = Day16.INPUT.charAt(i % Day16.INPUT.length()) - '0';
		}

		for (int i = 0; i < NUMBER_OF_ROUNDS; i++) {
			values = doIteration(values);
			System.out.println(i);
		}

		String output = Stream.ofAll(values).take(8).map(String::valueOf).collect(Collectors.joining());
		System.out.println(output);
	}

	private int[] doIteration(int[] values) {
		int[] temp = new int[values.length];
		for (int j = 0; j < values.length; j++) {
			for (int k = j; k < values.length; k++) {
				temp[j] += values[k];
			}
			temp[j] = temp[j] % 10;
		}
		return temp;
	}
}
