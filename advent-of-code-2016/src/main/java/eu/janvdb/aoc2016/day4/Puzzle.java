package eu.janvdb.aoc2016.day4;

import eu.janvdb.aocutil.java.Histogram;
import io.vavr.collection.Stream;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Puzzle {

	public static void main(String[] args) {
		new Puzzle().execute();
	}

	private void execute() {
		Stream.of(Input.ROOM_NAMES)
				.map(RoomName::new)
				.filter(RoomName::isValid)
				.map(RoomName::getUnshiftedName)
				.forEach(System.out::println);
	}

	private static class RoomName {

		private final String name;
		private final int index;
		private final String checksum;

		private static final Pattern ROOM_NAME_PATTERN = Pattern.compile(
				"([-a-z]+)-(\\d+)\\[([a-z]{5})]"
		);

		RoomName(String description) {
			Matcher matcher = ROOM_NAME_PATTERN.matcher(description);
			if (!matcher.matches()) {
				throw new IllegalArgumentException(description);
			}

			this.name = matcher.group(1);
			this.index = Integer.parseInt(matcher.group(2));
			this.checksum = matcher.group(3);
		}

		String getUnshiftedName() {
			return Stream.ofAll(name.toCharArray())
					.map(i -> i == '-' ? ' ' : ((i - 'a') + index) % 26 + 'a')
					.foldLeft(
							new StringBuilder(),
							StringBuilder::append
					)
					.toString();

		}

		boolean isValid() {
			Stream<Character> characters = Stream
					.ofAll(name.toCharArray())
					.filter(ch -> ch != '-');

			String calculatedChecksum = Histogram.createHistogram(characters)
					.take(5)
					.map(Histogram.HistogramEntry::getItem)
					.foldLeft(
							new StringBuilder(),
							StringBuilder::append
					).toString();

			return calculatedChecksum.equals(checksum);
		}
	}

}
