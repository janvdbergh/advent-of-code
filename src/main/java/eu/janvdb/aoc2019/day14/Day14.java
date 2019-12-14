package eu.janvdb.aoc2019.day14;

import eu.janvdb.util.InputReader;
import io.vavr.collection.List;

public class Day14 {

	private static final long ORE = 1_000_000_000_000L;
	public static final ProductQuantity ONE_FUEL = new ProductQuantity("FUEL", 1);

	public static void main(String[] args) {
		new Day14().run();
	}

	private void run() {
		List<Reaction> reactions = InputReader.readInput(Day14.class.getResource("input.txt"))
				.map(Reaction::parse)
				.toList();

		part1(reactions);
		part2(reactions);
	}

	private void part1(List<Reaction> reactions) {
		Reactor reactor = new Reactor(reactions);
		long oreRequired = reactor.oreRequiredFor(ONE_FUEL);
		System.out.println(oreRequired);
	}

	private void part2(List<Reaction> reactions) {
		Reactor reactor = new Reactor(reactions);
		System.out.println(reactor.maxProductForOre(ORE, ONE_FUEL));
	}
}
