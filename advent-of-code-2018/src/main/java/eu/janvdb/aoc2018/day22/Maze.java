package eu.janvdb.aoc2018.day22;

import eu.janvdb.aocutil.java.Point2D;
import eu.janvdb.aocutil.java.shortestpath.MapDescription;
import eu.janvdb.aocutil.java.shortestpath.ShortestPath;
import eu.janvdb.aocutil.java.shortestpath.ShortestPathBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Maze {

	private final Point2D target;
	private final Map<Point2D, Terrain> terrains = new HashMap<>();

	public Maze(Point2D target) {
		this.target = target;

		int finalX = target.getX() * 5;
		int finalY = target.getY() + 10;

		for (int y = 0; y <= finalY; y++) {
			for (int x = 0; x <= finalX; x++) {
				Point2D location = new Point2D(x, y);
				terrains.put(location, new Terrain(location));
			}
		}
	}

	public void print() {
		for (int y = 0; y <= target.getY(); y++) {
			for (int x = 0; x <= target.getX(); x++) {
				Point2D location = new Point2D(x, y);
				char ch;
				if (location.equals(Point2D.ORIGIN)) {
					ch = 'M';
				} else if (location.equals(Day22.TARGET)) {
					ch = 'T';
				} else {
					ch = switch (terrains.get(location).getType()) {
						case WET -> '=';
						case ROCKY -> '.';
						case NARROW -> '|';
					};
				}
				System.out.print(ch);
			}
			System.out.println();
		}
	}

	public int getRiskLevel() {
		return terrains.values().stream().mapToInt(terrain -> terrain.getType().getRiskLevel()).sum();
	}

	public int getFastestTimeToTarget() {
		State initialState = new State(Tool.TORCH, Point2D.ORIGIN, terrains.get(Point2D.ORIGIN).getType());
		State endState = new State(Tool.TORCH, Day22.TARGET, terrains.get(Day22.TARGET).getType());

		MazeMapDescription mazeMapDescription = new MazeMapDescription(initialState);
		ShortestPath<State> shortestPath = ShortestPathBuilder.build(mazeMapDescription);

		shortestPath.printRouteTo(endState);
		return shortestPath.distanceTo(endState);
	}

	public class MazeMapDescription implements MapDescription<State> {

		private final State initialState;

		public MazeMapDescription(State initialState) {
			this.initialState = initialState;
		}

		@Override
		public State getOrigin() {
			return initialState;
		}

		@Override
		public List<State> getNeighbours(State point) {
			return Stream.of(
					createState(Tool.NONE, point.getLocation()),
					createState(Tool.TORCH, point.getLocation()),
					createState(Tool.CLIMBING_GEAR, point.getLocation()),
					createState(point.getTool(), point.getLocation().move(0, -1)),
					createState(point.getTool(), point.getLocation().move(-1, 0)),
					createState(point.getTool(), point.getLocation().move(1, 0)),
					createState(point.getTool(), point.getLocation().move(0, 1))
			)
					.filter(state -> !state.equals(point))
					.filter(State::isValid)
					.collect(Collectors.toList());
		}

		private State createState(Tool tool, Point2D location) {
			Terrain terrain = terrains.get(location);
			TerrainType terrainType = terrain != null ? terrain.getType() : null;
			return new State(tool, location, terrainType);
		}

		@Override
		public int getDistance(State from, State to) {
			if (from.getTool() == to.getTool()) return 1;
			return 7;
		}
	}
}
