package eu.janvdb.aoc2016.day1;

import io.vavr.collection.List;

public class Puzzle {

	private static final String INPUT =
			"R3, L5, R2, L1, L2, R5, L2, R2, L2, L2, L1, R2, L2, R4, R4, R1, L2, L3, R3, L1, R2, L2, L4, R4, R5, L3, " +
					"R3, L3, L3, R4, R5, L3, R3, L5, L1, L2, R2, L1, R3, R1, L1, R187, L1, R2, R47, L5, L1, L2, R4, R3, L3, R3, " +
					"R4, R1, R3, L1, L4, L1, R2, L1, R4, R5, L1, R77, L5, L4, R3, L2, R4, R5, R5, L2, L2, R2, R5, L2, R194, R5, " +
					"L2, R4, L5, L4, L2, R5, L3, L2, L5, R5, R2, L3, R3, R1, L4, R2, L1, R5, L1, R5, L1, L1, R3, L1, R5, " +
					"R2, R5, R5, L4, L5, L5, L5, R3, L2, L5, L4, R3, R1, R1, R4, L2, L4, R5, R5, R4, L2, L2, R5, R5, L5, " +
					"L2, R4, R4, L4, R1, L3, R1, L1, L1, L1, L4, R5, R4, L4, L4, R5, R3, L2, L2, R3, R1, R4, L3, R1, L4, " +
					"R3, L3, L2, R2, R2, R2, L1, L4, R3, R2, R2, L3, R2, L3, L2, R4, L2, R3, L4, R5, R4, R1, R5, R3";

//	private static final String INPUT = "R8, R4, R4, R8";

	public static void main(String[] args) {
		String[] steps = INPUT.split("\\s*,\\s*");

		Location currentLocation = new Location(0, 0);
		Direction currentDirection = Direction.NORTH;

		List<Location> locations = List.of(currentLocation);

		for (String step : steps) {
			currentDirection = switch (step.charAt(0)) {
				case 'L' -> currentDirection.left();
				case 'R' -> currentDirection.right();
				default -> throw new IllegalArgumentException(step);
			};

			int numberOfSteps = Integer.parseInt(step.substring(1));
			for (int i = 0; i < numberOfSteps; i++) {
				currentLocation = currentLocation.step(currentDirection);
				if (locations.contains(currentLocation)) {
					System.out.println(currentLocation);
				}
				locations = locations.append(currentLocation);
			}
		}
	}

	private enum Direction {

		NORTH, EAST, SOUTH, WEST;

		Direction left() {
			return switch (this) {
				case NORTH -> WEST;
				case WEST -> SOUTH;
				case SOUTH -> EAST;
				case EAST -> NORTH;
			};
		}

		Direction right() {
			return switch (this) {
				case SOUTH -> WEST;
				case EAST -> SOUTH;
				case NORTH -> EAST;
				case WEST -> NORTH;
			};
		}
	}

	private static class Location implements Comparable<Location> {

		private final int x, y;

		private Location(int x, int y) {
			this.x = x;
			this.y = y;
		}

		Location step(Direction direction) {
			return switch (direction) {
				case NORTH -> new Location(x, y + 1);
				case EAST -> new Location(x + 1, y);
				case SOUTH -> new Location(x, y - 1);
				case WEST -> new Location(x - 1, y);
			};
		}

		@Override
		public String toString() {
			return "(" + x + ", " + y + ") - " + size();
		}

		private int size() {
			return Math.abs(x) + Math.abs(y);
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			Location location = (Location) o;

			return x == location.x && y == location.y;
		}

		@Override
		public int hashCode() {
			int result = x;
			result = 31 * result + y;
			return result;
		}

		@Override
		public int compareTo(Location o) {
			return size() - o.size();
		}
	}
}
