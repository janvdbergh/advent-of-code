package eu.janvdb.aoc2016.day24;

import eu.janvdb.util.Permutations;
import io.vavr.Tuple;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Seq;

public class Puzzle {

	public static void main(String[] args) {
		new Puzzle().execute();
	}

	private void execute() {
		Labyrinth labyrinth = Labyrinth.Reader.readFromFile(getClass().getResource("input.txt"));

		Map<Location, ShortestPath> shortestPaths = labyrinth.getSpecialLocations()
				.toMap(location -> Tuple.of(location._2, ShortestPath.Builder.createShortestPath(labyrinth, location._2)));

		Location startLocation = labyrinth.getSpecialLocations().get(0).get();
		Seq<Location> numberedLocations = labyrinth.getSpecialLocations().remove(0).values();
		int minDistance = Permutations.getAllPermutations(numberedLocations)
				.map(locations -> calculateDistance(startLocation, locations, shortestPaths))
				.min()
				.getOrElseThrow(IllegalStateException::new);

		System.out.println(minDistance);
	}

	private int calculateDistance(Location startLocation, List<Location> locations, Map<Location, ShortestPath> shortestPaths) {
		List<Location> fromLocations = locations.insert(0, startLocation);
		List<Location> toLocations = locations.append(startLocation);

		return fromLocations.zip(toLocations)
				.map(locationPair -> shortestPaths
						.get(locationPair._1).getOrElseThrow(IllegalStateException::new)
						.getDistanceTo(locationPair._2)
				)
				.sum().intValue();
	}
}
