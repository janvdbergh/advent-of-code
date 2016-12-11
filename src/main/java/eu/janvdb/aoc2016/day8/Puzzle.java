package eu.janvdb.aoc2016.day8;

import java.util.Arrays;

public class Puzzle {

	public static void main(String[] args) {
		new Puzzle().execute();
	}

	private void execute() {
		Display display = new Display(50, 6);

		Parser parser = new Parser();
		Arrays.stream(Input.INPUT)
				.map(parser::parse)
				.forEach(display::execute);

		System.out.println(display.countPixels());
	}

}
