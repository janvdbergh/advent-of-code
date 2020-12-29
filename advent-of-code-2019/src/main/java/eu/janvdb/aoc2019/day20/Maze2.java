package eu.janvdb.aoc2019.day20;

import eu.janvdb.aocutil.java.shortestpath.MapDescription;
import eu.janvdb.aocutil.java.shortestpath.ShortestPath;
import eu.janvdb.aocutil.java.shortestpath.ShortestPathBuilder;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Maze2 {

	private final Map<Portal, Map<Portal, Integer>> distances;
	private final Portal start;
	private final Portal end;

	public Maze2(Map<Portal, Map<Portal, Integer>> distances) {
		this.distances = distances;
		this.start = distances.keySet().stream().filter(Portal::isStart).findAny().orElseThrow();
		this.end = distances.keySet().stream().filter(Portal::isEnd).findAny().orElseThrow();
	}

	public int getMinimumDistance1() {
		ShortestPath<Map2State> shortestPath = ShortestPathBuilder.build(new Maze2MapDescription(0));

		Map2State endState = new Map2State(end, 0);
		shortestPath.printRouteTo(endState);
		return shortestPath.distanceTo(endState);
	}

	public int getMinimumDistance2() {
		Map2State endState = new Map2State(end, 0);

		int maxLevel = 10;
		ShortestPath<Map2State> shortestPath = ShortestPathBuilder.build(new Maze2MapDescription(maxLevel));
		while (!shortestPath.getReachablePoints().contains(endState)) {
			maxLevel += 5;
			shortestPath = ShortestPathBuilder.build(new Maze2MapDescription(maxLevel));
		}

		shortestPath.printRouteTo(endState);
		return shortestPath.distanceTo(endState);
	}

	private class Maze2MapDescription implements MapDescription<Map2State> {
		private final int maxLevel;

		private Maze2MapDescription(int maxLevel) {
			this.maxLevel = maxLevel;
		}

		@Override
		public Map2State getOrigin() {
			return new Map2State(start, 0);
		}

		@Override
		public List<Map2State> getNeighbours(Map2State state) {
			Portal portal = state.portal;
			int level = state.level;

			List<Map2State> result = distances.get(portal).keySet().stream()
					.map(p -> new Map2State(p, level))
					.collect(Collectors.toList());

			if (maxLevel == 0) {
				// Part 1
				if (portal.isInner() || portal.isOuter()) {
					result.add(new Map2State(otherSide(portal), level));
				}
			} else {
				// Part 2
				if (portal.isOuter() && level != 0) {
					result.add(new Map2State(otherSide(portal), level - 1));
				}
				if (portal.isInner() && level != maxLevel) {
					result.add(new Map2State(otherSide(portal), level + 1));
				}
			}

			return result;
		}

		private Portal otherSide(Portal portal) {
			return distances.keySet().stream()
					.filter(p -> p.getType() != portal.getType() && p.getLabel().equals(portal.getLabel()))
					.findAny().orElseThrow();
		}

		@Override
		public int getDistance(Map2State from, Map2State to) {
			if (from.level != to.level) return 1;
			if (from.portal.getLabel().equals(to.portal.getLabel())) return 1;

			return distances.get(from.portal).get(to.portal);
		}
	}

	private static class Map2State {
		private final Portal portal;
		private final int level;

		private Map2State(Portal portal, int level) {
			this.portal = portal;
			this.level = level;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			Map2State map2State = (Map2State) o;
			return level == map2State.level && portal.equals(map2State.portal);
		}

		@Override
		public int hashCode() {
			return Objects.hash(portal, level);
		}

		@Override
		public String toString() {
			return String.format("%s @ %d", portal, level);
		}
	}
}
