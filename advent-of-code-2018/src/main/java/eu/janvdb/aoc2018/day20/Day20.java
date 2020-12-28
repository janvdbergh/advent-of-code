package eu.janvdb.aoc2018.day20;

import eu.janvdb.aocutil.java.FileReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Day20 {

	private static final Location START = new Location(0, 0);

	public static void main(String[] args) throws IOException {
		String input = String.join("", FileReader.readStringFile(Day20.class, "day20.txt"));

		Network network = new Network();
		parse(input, network);

		network.print();

		network.calculateShortestPathFrom(START);

		// part 1
		network.getLocations().stream()
				.mapToInt(network::getDistance)
				.max()
				.ifPresent(System.out::println);

		// part 2
		long count = network.getLocations().stream()
				.mapToInt(network::getDistance)
				.filter(distance -> distance >= 1000)
				.count();
		System.out.println(count);
	}

	private static void parse(String input, Network network) {
		parse(input, Collections.singletonList(START), network);
	}

	private static List<Location> parse(String input, List<Location> locations, Network network) {
		int groupIndex = input.indexOf('(');

		if (groupIndex == -1) {
			return parseSimple(input, locations, network);
		} else {
			locations = parseSimple(input.substring(0, groupIndex), locations, network);
			return parseRest(input.substring(groupIndex), locations, network);
		}
	}

	private static List<Location> parseSimple(String input, List<Location> locations, Network network) {
		return locations.stream()
				.map(location -> parseSimple(input, location, network))
				.collect(Collectors.toList());
	}

	private static Location parseSimple(String input, Location location, Network network) {
		for (int i = 0; i < input.length(); i++) {
			Location nextLocation = switch (input.charAt(i)) {
				case 'N' -> location.north();
				case 'S' -> location.south();
				case 'E' -> location.east();
				case 'W' -> location.west();
				default -> null;
			};
			if (nextLocation != null) {
				network.addConnection(location, nextLocation);
				location = nextLocation;
			}
		}

		return location;
	}

	private static List<Location> parseRest(String input, List<Location> locations, Network network) {
		List<Integer> splitBoundaries = findSplitBoundaries(input);
		String rest = input.substring(splitBoundaries.get(splitBoundaries.size() - 1) + 1);
		return parse(rest, locations, network);
	}

	private static List<Integer> findSplitBoundaries(String input) {
		int count = 1;
		List<Integer> splitBoundaries = new ArrayList<>();
		splitBoundaries.add(0);
		int index2 = 1;
		while (count != 0) {
			char ch = input.charAt(index2);
			if (ch == '(') count++;
			if (ch == ')') count--;
			if (ch == '|' && count == 1) splitBoundaries.add(index2);
			index2++;
		}
		splitBoundaries.add(index2 - 1);
		return splitBoundaries;
	}
}