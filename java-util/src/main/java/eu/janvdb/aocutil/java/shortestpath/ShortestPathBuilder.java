package eu.janvdb.aocutil.java.shortestpath;

import eu.janvdb.aocutil.java.Pair;
import eu.janvdb.aocutil.java.Point2D;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class ShortestPathBuilder {

	public static ShortestPath build(MapDescription mapDescription) {
		Map<Point2D, List<Point2D>> neighbours = captureNeighbours(mapDescription);
		ShortestPathImpl shortestPath = createInitialShortestPath(mapDescription, neighbours);
		optimizeShortestPath(shortestPath, mapDescription, neighbours);

		return shortestPath;
	}

	private static Map<Point2D, List<Point2D>> captureNeighbours(MapDescription mapDescription) {
		Map<Point2D, List<Point2D>> result = new HashMap<>();

		Queue<Point2D> toDo = new LinkedList<>();
		toDo.add(mapDescription.getOrigin());
		while (!toDo.isEmpty()) {
			Point2D current = toDo.remove();
			if (result.containsKey(current)) continue;

			List<Point2D> neighbours = mapDescription.getNeighbours(current);
			result.put(current, neighbours);
			toDo.addAll(neighbours);
		}

		return result;
	}

	private static ShortestPathImpl createInitialShortestPath(MapDescription mapDescription, Map<Point2D, List<Point2D>> neighbours) {
		ShortestPathImpl shortestPath = new ShortestPathImpl(mapDescription.getOrigin());
		Queue<Pair<Point2D, Point2D>> toDo = new LinkedList<>();
		toDo.add(new Pair<>(mapDescription.getOrigin(), null));
		while (!toDo.isEmpty()) {
			Pair<Point2D, Point2D> current = toDo.remove();
			Point2D currentPoint = current._1();
			Point2D fromPoint = current._2();
			if (!shortestPath.reachablePoints.contains(currentPoint)) {
				int distance = fromPoint == null ? 0 : mapDescription.getDistance(fromPoint, currentPoint);
				shortestPath.registerPoint(currentPoint, fromPoint, distance);
				neighbours.get(currentPoint).forEach(neighbour -> toDo.add(new Pair<>(neighbour, currentPoint)));
			}
		}
		return shortestPath;
	}

	private static void optimizeShortestPath(ShortestPathImpl shortestPath, MapDescription mapDescription, Map<Point2D, List<Point2D>> neighbours) {
		Queue<Point2D> toDo = new LinkedList<>(shortestPath.reachablePoints);
		while (!toDo.isEmpty()) {
			Point2D from = toDo.remove();
			for (Point2D to : neighbours.get(from)) {
				int currentDistance = shortestPath.distanceMap.get(to);
				int newDistance = shortestPath.distanceMap.get(from) + mapDescription.getDistance(from, to);
				if (newDistance < currentDistance) {
					shortestPath.distanceMap.put(to, newDistance);
					shortestPath.stepMap.put(to, from);
					if (!toDo.contains(to)) toDo.add(to);
				}
			}
		}
	}

	private static class ShortestPathImpl implements ShortestPath {

		private final Point2D origin;
		private final Set<Point2D> reachablePoints = new HashSet<>();
		private final Map<Point2D, Integer> distanceMap = new HashMap<>();
		private final Map<Point2D, Point2D> stepMap = new HashMap<>();

		private ShortestPathImpl(Point2D origin) {
			this.origin = origin;
		}

		@Override
		public Point2D getOrigin() {
			return origin;
		}

		@Override
		public Set<Point2D> getReachablePoints() {
			return Collections.unmodifiableSet(reachablePoints);
		}

		@Override
		public int distanceTo(Point2D point) {
			return distanceMap.get(point);
		}

		@Override
		public Point2D stepTo(Point2D point) {
			return stepMap.get(point);
		}

		private void registerPoint(Point2D toPoint, Point2D fromPoint, int distance) {
			reachablePoints.add(toPoint);
			if (fromPoint==null) {
				distanceMap.put(toPoint, distance);
			} else {
				distanceMap.put(toPoint, distanceMap.get(fromPoint) + distance);
				stepMap.put(toPoint, fromPoint);
			}
		}
	}

}
