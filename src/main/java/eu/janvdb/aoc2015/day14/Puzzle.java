package eu.janvdb.aoc2015.day14;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javaslang.collection.List;
import javaslang.collection.Stream;
import javaslang.control.Option;

public class Puzzle {

//	private static final int DURATION = 1000;
	private static final int DURATION = 2503;

	private static final List<String> INPUT0 = List.of(
			"Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.",
			"Dancer can fly 16 km/s for 11 seconds, but then must rest for 162 seconds."
	);

	private static final List<String> INPUT = List.of(
			"Dancer can fly 27 km/s for 5 seconds, but then must rest for 132 seconds.",
			"Cupid can fly 22 km/s for 2 seconds, but then must rest for 41 seconds.",
			"Rudolph can fly 11 km/s for 5 seconds, but then must rest for 48 seconds.",
			"Donner can fly 28 km/s for 5 seconds, but then must rest for 134 seconds.",
			"Dasher can fly 4 km/s for 16 seconds, but then must rest for 55 seconds.",
			"Blitzen can fly 14 km/s for 3 seconds, but then must rest for 38 seconds.",
			"Prancer can fly 3 km/s for 21 seconds, but then must rest for 40 seconds.",
			"Comet can fly 18 km/s for 6 seconds, but then must rest for 103 seconds.",
			"Vixen can fly 18 km/s for 5 seconds, but then must rest for 84 seconds."
	);

	public static void main(String[] args) {
		List<Reindeer> reindeers = INPUT.map(Reindeer::new);

		Stream.range(1, DURATION).forEach(
				time -> {
					int max = reindeers.toStream()
							.map(reindeer -> reindeer.getDistance(time))
							.max().getOrElse(-1);
					reindeers.toStream()
							.filter(reindeer -> reindeer.getDistance(time) == max)
							.forEach(Reindeer::addPoint);
				}
		);

		Option<Integer> max = reindeers.toStream()
				.map(Reindeer::getPoints)
				.max();

		System.out.println(max);
	}

	public static class Reindeer {

		private static final Pattern PATTERN = Pattern.compile("(\\w+) can fly (\\d+) km/s for (\\d+) seconds, but then must rest for (\\d+) seconds.");

		private final String name;
		private final int speed;
		private final int flightTime;
		private final int restingTime;
		private int points;

		public Reindeer(String description) {
			Matcher matcher = PATTERN.matcher(description);
			if (!matcher.matches()) {
				throw new IllegalArgumentException(description);
			}

			name = matcher.group(1);
			speed = Integer.parseInt(matcher.group(2));
			flightTime = Integer.parseInt(matcher.group(3));
			restingTime = Integer.parseInt(matcher.group(4));
		}

		public int getDistance(int time) {
			int totalTime = flightTime + restingTime;
			int fullCycles = time / totalTime;
			int remainingFlightTime = Math.min(flightTime, time % totalTime);

			return (fullCycles * flightTime + remainingFlightTime) * speed;
		}

		public void addPoint() {
			points++;
		}

		public int getPoints() {
			return points;
		}
	}
}
