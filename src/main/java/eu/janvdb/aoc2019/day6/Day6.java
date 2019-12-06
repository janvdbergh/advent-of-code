package eu.janvdb.aoc2019.day6;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import eu.janvdb.util.InputReader;

public class Day6 {

	public static void main(String[] args) {
		new Day6().run();
	}

	private void run() {
		Map<String, String> orbitMap = InputReader.readInput(Day6.class.getResource("day6.txt"))
				.map(line -> line.split("\\)"))
				.collect(Collectors.toMap(parts -> parts[1], parts -> parts[0]));

		part1(orbitMap);
		part2(orbitMap);
	}

	private void part1(Map<String, String> orbitMap) {
		int orbitCount = orbitMap.keySet().stream()
				.mapToInt(key -> countOrbits(key, orbitMap))
				.sum();
		System.out.println(orbitCount);
	}

	private int countOrbits(String key, Map<String, String> orbitMap) {
		int count = 0;
		while (orbitMap.containsKey(key)) {
			count++;
			key = orbitMap.get(key);
		}
		return count;
	}

	private void part2(Map<String, String> orbitMap) {
		String startPoint = orbitMap.get("YOU");
		String endPoint = orbitMap.get("SAN");

		Map<String, Integer> bestDistances = buildDistanceMap(orbitMap, startPoint);
		System.out.println(bestDistances.get(endPoint));
	}

	private Map<String, Integer> buildDistanceMap(Map<String, String> orbitMap, String startPoint) {
		Map<String, Integer> bestDistances = new HashMap<>();
		bestDistances.put(startPoint, 0);

		boolean iterate = true;
		while (iterate) {
			iterate = false;
			for (String from : orbitMap.keySet()) {
				String to = orbitMap.get(from);

				iterate |= addDistance(bestDistances, from, to);
				iterate |= addDistance(bestDistances, to, from);
			}
		}
		return bestDistances;
	}

	private boolean addDistance(Map<String, Integer> bestDistances, String point1, String point2) {
		int distance1 = bestDistances.getOrDefault(point1, Integer.MAX_VALUE / 2);
		int distance2 = bestDistances.getOrDefault(point2, Integer.MAX_VALUE / 2);
		if (distance1 > distance2 + 1) {
			bestDistances.put(point1, distance2 + 1);
			return true;
		}
		return false;
	}
}
