package eu.janvdb.aoc2016.day24;

import javaslang.collection.Stream;

public class ShortestPath {

	private static final int MAX = Integer.MAX_VALUE / 2;
	private final Labyrinth labyrinth;
	private final int[] distances;

	private ShortestPath(Labyrinth labyrinth, int[] distances) {
		this.labyrinth = labyrinth;
		this.distances = distances;
	}

	public int getDistanceTo(Location location) {
		return distances[labyrinth.getIndex(location)];
	}

	private int getDistanceTo(int row, int column) {
		return distances[labyrinth.getIndex(row, column)];
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int row = 0; row < labyrinth.getHeight(); row++) {
			for (int column = 0; column < labyrinth.getWidth(); column++) {
				int distance = getDistanceTo(row, column);
				if (distance == MAX) {
					builder.append('#');
				} else {
					builder.append(distance);
				}
			}
			builder.append('\n');
		}
		return builder.toString();
	}

	public static class Builder {

		private final Labyrinth labyrinth;
		private final Location startLocation;
		private final int[] distances;

		public static ShortestPath createShortestPath(Labyrinth labyrinth, Location startLocation) {
			return new Builder(labyrinth, startLocation).getShortestPath();
		}

		private Builder(Labyrinth labyrinth, Location startLocation) {
			this.labyrinth = labyrinth;
			this.startLocation = startLocation;
			this.distances = new int[labyrinth.getWidth() * labyrinth.getHeight()];
		}

		@SuppressWarnings("StatementWithEmptyBody")
		private ShortestPath getShortestPath() {
			initializeDistances();
			while (optimizeDistances()) ;

			return new ShortestPath(labyrinth, distances);
		}

		private boolean optimizeDistances() {
			boolean result = false;

			// optimization: there is a wall surrounding the labyrinth
			for (int row = 1; row < labyrinth.getHeight(); row++) {
				for (int column = 1; column < labyrinth.getWidth(); column++) {
					if (!labyrinth.isWall(row, column)) {
						int newDistance = 1 + min(
								getDistance(row - 1, column),
								getDistance(row + 1, column),
								getDistance(row, column - 1),
								getDistance(row, column + 1)
						);

						if (getDistance(row, column) > newDistance) {
							setDistance(row, column, newDistance);
							result = true;
						}
					}
				}
			}

			return result;
		}

		private int min(int... values) {
			return Stream.ofAll(values).min().getOrElse(MAX);
		}

		private void initializeDistances() {
			for (int i = 0; i < distances.length; i++) {
				distances[i] = MAX;
			}
			setDistance(startLocation.getRow(), startLocation.getColumn(), 0);
		}

		private void setDistance(int row, int column, int distance) {
			distances[labyrinth.getIndex(row, column)] = distance;
		}

		private int getDistance(int row, int column) {
			return distances[labyrinth.getIndex(row, column)];
		}
	}
}
