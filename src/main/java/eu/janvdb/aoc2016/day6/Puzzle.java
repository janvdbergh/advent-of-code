package eu.janvdb.aoc2016.day6;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import eu.janvdb.util.Histogram;

public class Puzzle {

	public static void main(String[] args) {
		new Puzzle().execute();
	}

	private void execute() {
		IntStream.range(0, Input.WORDS[0].length()).forEach(position -> {
			List<Character> characters = Arrays.stream(Input.WORDS)
					.map(word -> word.charAt(position))
					.collect(Collectors.toList());

			List<Histogram.HistogramEntry<Character>> histogram = Histogram.createHistogram(characters);

			System.out.println(histogram.get(histogram.size()-1).getItem());
		});
	}
}
