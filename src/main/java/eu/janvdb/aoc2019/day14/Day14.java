package eu.janvdb.aoc2019.day14;

import eu.janvdb.util.InputReader;
import io.vavr.collection.List;

public class Day14 {

	private static final long ORE = 1_000_000_000_000L;

	public static void main(String[] args) {
		new Day14().run();
	}

	private void run() {
		List<Reaction> reactions = InputReader.readInput(Day14.class.getResource("input.txt"))
				.map(Reaction::parse)
				.toList();

		Reactor reactor = new Reactor(reactions);
		part1(reactor);
		part2(reactor);
	}

	private void part1(Reactor reactor) {
		getRequiredOreForFuel(reactor, 1L);
	}

	private void part2(Reactor reactor) {
		long min = 1L;
		long max = 1_000_000_000L;

		while (min < max) {
			long middle = (min + max) / 2;
			long ore = getRequiredOreForFuel(reactor, middle);

			if (ore < ORE) {
				min = middle + 1;
			} else {
				max = middle - 1;
			}
		}
		getRequiredOreForFuel(reactor, min);
	}

	private long getRequiredOreForFuel(Reactor reactor, long fuel) {
		long ore = reactor.oreRequiredFor(new ProductQuantity("FUEL", fuel));
		System.out.printf("%d fuel => %d ore\n", fuel, ore);
		return ore;
	}
}
