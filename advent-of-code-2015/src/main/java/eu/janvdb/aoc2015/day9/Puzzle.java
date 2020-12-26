package eu.janvdb.aoc2015.day9;

import io.vavr.collection.HashMap;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Set;
import io.vavr.collection.Stream;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Puzzle {

	private static final List<String> INPUT0 = List.of(
			"London to Dublin = 464",
			"London to Belfast = 518",
			"Dublin to Belfast = 141"
	);

	private static final List<String> INPUT = List.of(
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
	);

	private static final Pattern INPUT_PATTERN = Pattern.compile("([a-zA-Z]+) to ([a-zA-Z]+) = ([\\d]+)");

	public static void main(String[] args) {
		DistanceMap distanceMap = buildDistanceMap(INPUT);
		List<Route> routes = findRoutes(distanceMap).toList();

		routes.forEach(System.out::println);

		System.out.println(
				routes.toStream()
						.map(Route::getDistance)
						.max()
						.getOrElse(-1)
		);
	}

	private static DistanceMap buildDistanceMap(List<String> data) {
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

	private static Stream<Route> findRoutes(DistanceMap distanceMap) {
		return findRoutes(distanceMap, List.empty());
	}

	private static Stream<Route> findRoutes(DistanceMap distanceMap, List<String> usedPlaces) {
		if (usedPlaces.size() == distanceMap.places.size()) {
			return Stream.of(new Route(usedPlaces, getDistance(distanceMap, usedPlaces)));
		} else {
			return distanceMap.places.toStream()
					.filter(place -> !usedPlaces.contains(place))
					.flatMap(place -> findRoutes(distanceMap, usedPlaces.append(place)));
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

		private Set<String> places = HashSet.empty();
		private Map<String, Integer> distances = HashMap.empty();

		void storeDistance(String from, String to, int distance) {
			places = places
					.add(from)
					.add(to);

			distances = distances
					.put(getKey(from, to), distance)
					.put(getKey(to, from), distance);
		}

		private String getKey(String from, String to) {
			return from + "-" + to;
		}

		int getDistance(String from, String to) {
			String key = getKey(from, to);
			return distances
					.get(key)
					.getOrElseThrow(() -> new RuntimeException("Unknown key " + key));
		}
	}

	private static class Route {

		private final List<String> places;
		private final int distance;

		Route(List<String> places, int distance) {
			this.places = places;
			this.distance = distance;
		}

		@Override
		public String toString() {
			return String.join("-", places) + ": " + distance;
		}

		int getDistance() {
			return distance;
		}
	}
}
