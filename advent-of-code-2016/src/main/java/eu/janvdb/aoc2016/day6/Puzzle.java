package eu.janvdb.aoc2016.day6;

import eu.janvdb.aocutil.java.Histogram;
import io.vavr.collection.Stream;

public class Puzzle {

	public static void main(String[] args) {
		new Puzzle().execute();
	}

	private void execute() {
		int wordLength = Input.WORDS[0].length();
		Stream.range(0, wordLength)
				.map(index -> Stream.of(Input.WORDS).map(word -> word.charAt(index)))
				.map(Histogram::createHistogram)
				.map(histogram -> histogram.get(histogram.size() - 1).getItem())
				.forEach(System.out::println);
	}
}
