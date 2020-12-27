package eu.janvdb.aoc2018.day15;

import eu.janvdb.aocutil.java.Point2D;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.SortedSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Maze {

	private final int width, height;
	private final boolean[][] walls;
	private final SortedSet<Actor> actors;
	private int numberOfTurns;

	static Maze parse(List<String> description) {
		int height = description.size();
		int width = description.stream().mapToInt(String::length).max().orElseThrow();
		boolean[][] walls = new boolean[height][];
		SortedSet<Actor> actors = ActorComparator.newSortedSet();

		for (int y = 0; y < height; y++) {
			walls[y] = new boolean[width];
			String line = description.get(y);
			for (int x = 0; x < line.length(); x++) {
				if (line.charAt(x) == '#') walls[y][x] = true;
				if (line.charAt(x) == 'E') actors.add(new Actor(new Point2D(x, y), ActorType.ELF));
				if (line.charAt(x) == 'G') actors.add(new Actor(new Point2D(x, y), ActorType.GOBLIN));
			}
		}

		return new Maze(width, height, walls, actors);
	}

	private Maze(int width, int height, boolean[][] walls, SortedSet<Actor> actors) {
		this.width = width;
		this.height = height;
		this.walls = walls;
		this.actors = actors;
		actors.forEach(actor -> actor.setMap(this));
	}

	void executeTurn() {
		Queue<Actor> actorsToMove = new LinkedList<>(actors);

		while (!isFinished() && !actorsToMove.isEmpty()) {
			Actor actor = actorsToMove.remove();
			if (!actors.contains(actor)) continue;

			actors.remove(actor);
			actor.executeTurn();
			actors.add(actor);

			actors.removeIf(anyActor -> anyActor.getHitPoints() <= 0);
		}

		if (actorsToMove.isEmpty()) numberOfTurns++;
	}

	void print() {
		System.out.printf("After %d turns%n", numberOfTurns);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Point2D location = new Point2D(x, y);
				if (isWall(location))
					System.out.print('#');
				else if (isActorOfType(location, ActorType.ELF))
					System.out.print('E');
				else if (isActorOfType(location, ActorType.GOBLIN))
					System.out.print('G');
				else
					System.out.print('.');
			}

			int theY = y;
			actors.stream()
					.filter(actor -> actor.getLocation().getY() == theY)
					.forEach(actor -> System.out.printf("  %s(%d)", actor.getType().name().charAt(0), actor.getHitPoints()));
			System.out.println();
		}
		System.out.println();
	}

	SortedSet<Actor> getElves() {
		return actors.stream()
				.filter(actor -> actor.getType() == ActorType.ELF)
				.collect(Collectors.toCollection(ActorComparator::newSortedSet));
	}

	SortedSet<Actor> getGoblins() {
		return actors.stream()
				.filter(actor -> actor.getType() == ActorType.GOBLIN)
				.collect(Collectors.toCollection(ActorComparator::newSortedSet));
	}

	List<Point2D> getNeighbours(Point2D point) {
		return Stream.of(point.move(0, -1), point.move(-1, 0), point.move(1, 0), point.move(0, 1))
				.filter(p -> !isWall(p))
				.collect(Collectors.toList());
	}

	boolean isWall(Point2D location) {
		return walls[location.getY()][location.getX()];
	}

	boolean isFinished() {
		return actors.stream().map(Actor::getType).distinct().count() == 1;
	}

	int getScore() {
		int totalHitPoints = actors.stream().mapToInt(Actor::getHitPoints).sum();
		return numberOfTurns * totalHitPoints;
	}

	Actor getActorAt(Point2D location) {
		return actors.stream()
				.filter(actor -> Objects.equals(location, actor.getLocation()))
				.findAny()
				.orElse(null);
	}

	private boolean isActorOfType(Point2D location, ActorType type) {
		return actors.stream()
				.anyMatch(actor -> Objects.equals(actor.getLocation(), location) && actor.getType() == type);
	}
}
