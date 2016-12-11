package eu.janvdb.aoc2016.day4;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import eu.janvdb.util.Histogram;

public class Puzzle {

	public static void main(String[] args) {
		new Puzzle().execute();
	}

	private void execute() {
		Arrays.stream(Input.ROOM_NAMES)
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

		public RoomName(String description) {
			Matcher matcher = ROOM_NAME_PATTERN.matcher(description);
			if (!matcher.matches()) {
				throw new IllegalArgumentException(description);
			}

			this.name = matcher.group(1);
			this.index = Integer.parseInt(matcher.group(2));
			this.checksum = matcher.group(3);
		}

		public int getIndex() {
			return index;
		}
		
		public String getUnshiftedName() {
			return name.chars()
					.map(i -> i=='-' ? ' ' : ((i - 'a') + index) % 26 + 'a')
					.collect(
							() -> new StringBuilder(index + ": "),
							(stringBuilder, ch) -> stringBuilder.append((char) ch),
							StringBuilder::append
					)
					.toString();

		}

		public boolean isValid() {
			List<Integer> characters = name.chars()
					.filter(ch -> ch != '-')
					.mapToObj(ch -> ch)
					.collect(Collectors.toList());

			List<Histogram.HistogramEntry<Integer>> histogram = Histogram.createHistogram(characters);
			String calculatedChecksum = histogram.stream()
					.limit(5)
					.collect(
							StringBuilder::new,
							(stringBuilder, histogramEntry) -> stringBuilder.append((char) histogramEntry.getItem().intValue()),
							StringBuilder::append
					)
					.toString();

			return calculatedChecksum.equals(checksum);
		}

	}

}
