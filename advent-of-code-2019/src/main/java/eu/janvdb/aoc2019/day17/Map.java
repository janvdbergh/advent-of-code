package eu.janvdb.aoc2019.day17;

import eu.janvdb.aocutil.java.Direction;
import eu.janvdb.aocutil.java.Point2D;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Set;

public class Map {

	private final Set<Point2D> scaffolds;
	private final Direction robotDirection;
	private final Point2D robotLocation;

	public Map(String mapStr) {
		Set<Point2D> scaffolds = HashSet.empty();
		Direction robotDirection = null;
		Point2D robotLocation = null;

		String[] lines = mapStr.split("\\n");
		for (int y = 0; y < lines.length; y++) {
			for (int x = 0; x < lines[y].length(); x++) {
				char ch = lines[y].charAt(x);
				if (ch != '.') {
					Point2D point = new Point2D(x, y);
					scaffolds = scaffolds.add(point);
					if (ch != '#') {
						robotLocation = point;
						robotDirection = mapRobotDirection(ch);
					}
				}
			}
		}

		this.scaffolds = scaffolds;
		this.robotDirection = robotDirection;
		this.robotLocation = robotLocation;
	}

	private Direction mapRobotDirection(char ch) {
		return switch (ch) {
			case '^' -> Direction.NORTH;
			case '>' -> Direction.EAST;
			case 'V' -> Direction.SOUTH;
			case '<' -> Direction.WEST;
			default -> throw new IllegalArgumentException(String.valueOf(ch));
		};
	}

	public Set<Point2D> getIntersections() {
		return scaffolds.filter(this::isIntersection);
	}

	private boolean isIntersection(Point2D point) {
		return scaffolds.contains(point)
				&& scaffolds.contains(point.move(0, 1))
				&& scaffolds.contains(point.move(0, -1))
				&& scaffolds.contains(point.move(1, 0))
				&& scaffolds.contains(point.move(-1, 0));
	}

	public List<String> getWalkInstructions() {
		Point2D currentLocation = this.robotLocation;
		Direction currentDirection = this.robotDirection;
		Set<Point2D> mapRemaining = this.scaffolds;
		List<String> result = List.empty();

		print(mapRemaining, currentLocation);

		while (mapRemaining.size()>1) {
			int currentSteps = 0;
			while (mapRemaining.contains(currentLocation.step(currentDirection))) {
				currentSteps++;

				if (countNeighbours(mapRemaining, currentLocation) < 3) {
					mapRemaining = mapRemaining.remove(currentLocation);
				}
				currentLocation = currentLocation.step(currentDirection);
			}

			if (currentSteps != 0) {
				result = result.append(String.valueOf(currentSteps));
			}

			Direction walkDirection = findWalkDirection(mapRemaining, currentLocation);
			if (currentDirection.left() == walkDirection) {
				result = result.append("L");
			} else if (currentDirection.right() == walkDirection) {
				result = result.append("R");
			}
			currentDirection = walkDirection;
		}

		return result;
	}

	private int countNeighbours(Set<Point2D> mapRemaining, Point2D currentLocation) {
		return List.of(Direction.values()).map(currentLocation::step).count(mapRemaining::contains);
	}

	private Direction findWalkDirection(Set<Point2D> mapRemaining, Point2D currentLocation) {
		for (Direction direction : Direction.values()) {
			if (mapRemaining.contains(currentLocation.step(direction))) {
				return direction;
			}
		}
		return null;
	}

	public void print() {
		print(scaffolds, robotLocation);
	}

	private void print(Set<Point2D> map, Point2D currentLocation) {
		int minX = map.minBy(Point2D::getX).map(Point2D::getX).getOrElse(0);
		int maxX = map.maxBy(Point2D::getX).map(Point2D::getX).getOrElse(0);
		int minY = map.minBy(Point2D::getY).map(Point2D::getY).getOrElse(0);
		int maxY = map.maxBy(Point2D::getY).map(Point2D::getY).getOrElse(0);

		for (int y = minY; y <= maxY; y++) {
			for (int x = minX; x <= maxX; x++) {
				Point2D point = new Point2D(x, y);
				if (map.contains(point)) {
					if (point.equals(currentLocation)) {
						System.out.print('X');
					} else {
						System.out.print('#');
					}
				} else {
					if (point.equals(currentLocation)) {
						System.out.print('?');
					} else {
						System.out.print(' ');
					}
				}
			}
			System.out.println();
		}
		System.out.println();
	}
}
