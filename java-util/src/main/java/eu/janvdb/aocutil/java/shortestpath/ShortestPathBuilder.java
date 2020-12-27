package eu.janvdb.aocutil.java.shortestpath;

import eu.janvdb.aocutil.java.Pair;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public class ShortestPathBuilder {

	public static <T> ShortestPath<T> build(MapDescription<T> mapDescription) {
		Map<T, List<T>> neighbours = captureNeighbours(mapDescription);
		ShortestPathImpl<T> shortestPath = createInitialShortestPath(mapDescription, neighbours);
		optimizeShortestPath(shortestPath, mapDescription, neighbours);

		return shortestPath;
	}

	private static <T> Map<T, List<T>> captureNeighbours(MapDescription<T> mapDescription) {
		Map<T, List<T>> result = new HashMap<>();

		Queue<T> toDo = new LinkedList<>();
		toDo.add(mapDescription.getOrigin());
		while (!toDo.isEmpty()) {
			T current = toDo.remove();
			if (result.containsKey(current)) continue;

			List<T> neighbours = mapDescription.getNeighbours(current);
			result.put(current, neighbours);
			toDo.addAll(neighbours);
		}

		return result;
	}

	private static <T> ShortestPathImpl<T> createInitialShortestPath(MapDescription<T> mapDescription, Map<T, List<T>> neighbours) {
		ShortestPathImpl<T> shortestPath = new ShortestPathImpl<>(mapDescription.getOrigin());
		Queue<Pair<T, T>> toDo = new LinkedList<>();
		toDo.add(new Pair<>(mapDescription.getOrigin(), null));
		while (!toDo.isEmpty()) {
			Pair<T, T> current = toDo.remove();
			T currentPoint = current._1();
			T fromPoint = current._2();
			if (!shortestPath.reachablePoints.contains(currentPoint)) {
				int distance = fromPoint == null ? 0 : mapDescription.getDistance(fromPoint, currentPoint);
				shortestPath.registerPoint(currentPoint, fromPoint, distance);
				neighbours.get(currentPoint).forEach(neighbour -> toDo.add(new Pair<>(neighbour, currentPoint)));
			}
		}
		return shortestPath;
	}

	private static <T> void optimizeShortestPath(ShortestPathImpl<T> shortestPath, MapDescription<T> mapDescription, Map<T, List<T>> neighbours) {
		Stack<T> toDo = new Stack<>();
		toDo.addAll(shortestPath.reachablePoints);
		Set<T> toDoSet = new HashSet<>(shortestPath.reachablePoints);
		while (!toDo.isEmpty()) {
			T from = toDo.pop();
			toDoSet.remove(from);

			for (T to : neighbours.get(from)) {
				int currentDistance = shortestPath.distanceMap.get(to);
				int newDistance = shortestPath.distanceMap.get(from) + mapDescription.getDistance(from, to);
				if (newDistance < currentDistance) {
					shortestPath.distanceMap.put(to, newDistance);
					shortestPath.stepMap.put(to, from);

					if (!toDoSet.contains(to)) {
						toDo.add(to);
						toDoSet.add(to);
					}
				}
			}
		}
	}

	private static class ShortestPathImpl<T> implements ShortestPath<T> {

		private final T origin;
		private final Set<T> reachablePoints = new HashSet<>();
		private final Map<T, Integer> distanceMap = new HashMap<>();
		private final Map<T, T> stepMap = new HashMap<>();

		private ShortestPathImpl(T origin) {
			this.origin = origin;
		}

		@Override
		public T getOrigin() {
			return origin;
		}

		@Override
		public Set<T> getReachablePoints() {
			return Collections.unmodifiableSet(reachablePoints);
		}

		@Override
		public int distanceTo(T point) {
			return distanceMap.get(point);
		}

		@Override
		public T stepTo(T point) {
			return stepMap.get(point);
		}

		@Override
		public void printRouteTo(T point) {
			if (!point.equals(origin)) {
				printRouteTo(stepTo(point));
			}
			System.out.println(point);
		}

		private void registerPoint(T toPoint, T fromPoint, int distance) {
			reachablePoints.add(toPoint);
			if (fromPoint == null) {
				distanceMap.put(toPoint, distance);
			} else {
				distanceMap.put(toPoint, distanceMap.get(fromPoint) + distance);
				stepMap.put(toPoint, fromPoint);
			}
		}
	}

}
