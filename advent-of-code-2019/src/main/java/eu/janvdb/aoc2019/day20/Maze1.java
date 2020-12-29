package eu.janvdb.aoc2019.day20;

import eu.janvdb.aocutil.java.Point2D;
import eu.janvdb.aocutil.java.shortestpath.MapDescription;
import eu.janvdb.aocutil.java.shortestpath.ShortestPath;
import eu.janvdb.aocutil.java.shortestpath.ShortestPathBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Maze1 {

	private final boolean[][] walls;
	private final List<Portal> portals;
	private final int width;
	private final int height;

	public Maze1(boolean[][] walls, List<Portal> portals) {
		this.walls = walls;
		this.width = walls[0].length;
		this.height = walls.length;
		this.portals = portals;
	}

	public Map<Portal, Map<Portal, Integer>> getMinimumDistances() {
		return portals.stream().collect(Collectors.toMap(
				portal -> portal,
				portal -> {
					ShortestPath<Point2D> shortestPath = ShortestPathBuilder.build(new Maze1MapDescription(portal.getCoordinate()));
					return shortestPath.getReachablePoints().stream()
							.map(this::getPortalByCoordinate)
							.filter(Optional::isPresent)
							.map(Optional::get)
							.filter(p -> !p.equals(portal))
							.collect(Collectors.toMap(
									key -> key,
									key -> shortestPath.distanceTo(key.getCoordinate())
							));
				}
		));
	}

	private boolean isWall(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height) return true;
		return walls[y][x];
	}

	private Optional<Portal> getPortalByCoordinate(Point2D point) {
		return portals.stream().filter(p -> p.getCoordinate().equals(point)).findAny();
	}

	private class Maze1MapDescription implements MapDescription<Point2D> {

		private final Point2D origin;

		public Maze1MapDescription(Point2D origin) {
			this.origin = origin;
		}

		@Override
		public Point2D getOrigin() {
			return origin;
		}

		@Override
		public List<Point2D> getNeighbours(Point2D point) {
			if (getPortalByCoordinate(point).filter(Portal::isEnd).isPresent()) return Collections.emptyList();

			return Stream.of(
					point.move(0, -1),
					point.move(-1, 0),
					point.move(1, 0),
					point.move(0, 1)
			)
					.filter(Objects::nonNull)
					.filter(newPoint -> !isWall(newPoint.getX(), newPoint.getY()))
					.collect(Collectors.toList());
		}

		@Override
		public int getDistance(Point2D from, Point2D to) {
			return 1;
		}

		@Override
		public String getDescription(Point2D point) {
			return getPortalByCoordinate(point)
					.map(portal -> String.format("%s %s", point, portal))
					.orElseGet(point::toString);
		}
	}
}
