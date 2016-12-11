package eu.janvdb.aoc2015.day9;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Puzzle {

	private static final String[] INPUT0 = {
			"London to Dublin = 464",
			"London to Belfast = 518",
			"Dublin to Belfast = 141"
	};

	private static final String[] INPUT = {
			"Faerun to Norrath = 129",
			"Faerun to Tristram = 58",
			"Faerun to AlphaCentauri = 13",
			"Faerun to Arbre = 24",
			"Faerun to Snowdin = 60",
			"Faerun to Tambi = 71",
			"Faerun to Straylight = 67",
			"Norrath to Tristram = 142",
			"Norrath to AlphaCentauri = 15",
			"Norrath to Arbre = 135",
			"Norrath to Snowdin = 75",
			"Norrath to Tambi = 82",
			"Norrath to Straylight = 54",
			"Tristram to AlphaCentauri = 118",
			"Tristram to Arbre = 122",
			"Tristram to Snowdin = 103",
			"Tristram to Tambi = 49",
			"Tristram to Straylight = 97",
			"AlphaCentauri to Arbre = 116",
			"AlphaCentauri to Snowdin = 12",
			"AlphaCentauri to Tambi = 18",
			"AlphaCentauri to Straylight = 91",
			"Arbre to Snowdin = 129",
			"Arbre to Tambi = 53",
			"Arbre to Straylight = 40",
			"Snowdin to Tambi = 15",
			"Snowdin to Straylight = 99",
			"Tambi to Straylight = 70"
	};

	private static final Pattern INPUT_PATTERN = Pattern.compile("([a-zA-Z]+) to ([a-zA-Z]+) = ([\\d]+)");

	public static void main(String[] args) {
		DistanceMap distanceMap = buildDistanceMap(INPUT);
		List<Route> routes = findRoutes(distanceMap);

		routes.forEach(System.out::println);
		System.out.println(routes.stream().mapToInt(Route::getDistance).max().orElse(-1));
	}

	private static DistanceMap buildDistanceMap(String[] data) {
		DistanceMap distanceMap = new DistanceMap();
		for (String item : data) {
			Matcher matcher = INPUT_PATTERN.matcher(item);
			if (!matcher.matches()) {
				throw new IllegalArgumentException(item);
			}
			distanceMap.storeDistance(matcher.group(1), matcher.group(2), Integer.parseInt(matcher.group(3)));
		}

		return distanceMap;
	}

	private static List<Route> findRoutes(DistanceMap distanceMap) {
		List<Route> result = new ArrayList<>();
		List<String> usedPlaces = new ArrayList<>();

		findRoutes(distanceMap, usedPlaces, result);
		return result;
	}

	private static void findRoutes(DistanceMap distanceMap, List<String> usedPlaces, List<Route> result) {
		if (usedPlaces.size() == distanceMap.places.size()) {
			result.add(new Route(usedPlaces, getDistance(distanceMap, usedPlaces)));
		} else {
			distanceMap.places.stream()
					.filter(place -> !usedPlaces.contains(place))
					.forEach(place -> {
						List<String> newPlaces = new ArrayList<>(usedPlaces);
						newPlaces.add(place);
						findRoutes(distanceMap, newPlaces, result);
					});
		}
	}

	private static int getDistance(DistanceMap distanceMap, List<String> usedPlaces) {
		int totalDistance = 0;
		for (int i = 0; i < usedPlaces.size() - 1; i++) {
			totalDistance += distanceMap.getDistance(usedPlaces.get(i), usedPlaces.get(i + 1));
		}

		return totalDistance;
	}

	private static class DistanceMap {

		private Set<String> places = new HashSet<>();
		private Map<String, Integer> distances = new HashMap<>();

		public void storeDistance(String from, String to, int distance) {
			places.add(from);
			places.add(to);
			distances.put(getKey(from, to), distance);
			distances.put(getKey(to, from), distance);
		}

		private String getKey(String from, String to) {
			return from + "-" + to;
		}

		public int getDistance(String from, String to) {
			return distances.get(getKey(from, to));
		}
	}

	private static class Route {

		private final List<String> places;
		private int distance;

		public Route(List<String> places, int distance) {
			this.places = places;
			this.distance = distance;
		}

		@Override
		public String toString() {
			return String.join("-", places) + ": " + distance;
		}

		public int getDistance() {
			return distance;
		}
	}


}
