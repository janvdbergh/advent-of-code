package eu.janvdb.aoc2018.day15;

import eu.janvdb.aocutil.java.Point2D;
import eu.janvdb.aocutil.java.shortestpath.MapDescription;
import eu.janvdb.aocutil.java.shortestpath.ShortestPath;
import eu.janvdb.aocutil.java.shortestpath.ShortestPathBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.UUID;
import java.util.stream.Collectors;

class Actor {

	private final String id = UUID.randomUUID().toString();
	private final ActorType type;
	private final int attackPower;

	private Maze maze;
	private Point2D location;
	private int hitPoints;

	Actor(Point2D initialLocation, ActorType type) {
		this.type = type;
		this.location = initialLocation;
		this.hitPoints = type.getInitialHitPoints();
		this.attackPower = type.getAttackPower();
	}

	void setMap(Maze maze) {
		this.maze = maze;
	}

	Point2D getLocation() {
		return location;
	}

	ActorType getType() {
		return type;
	}

	int getHitPoints() {
		return hitPoints;
	}

	void executeTurn() {
		SortedSet<Actor> enemies = type == ActorType.ELF ? maze.getGoblins() : maze.getElves();
		Set<Point2D> enemyOpenSquares = enemies.stream()
				.map(Actor::getLocation)
				.flatMap(location -> maze.getNeighbours(location).stream())
				.distinct()
				.filter(point -> maze.getActorAt(point) == null)
				.collect(Collectors.toSet());

		if (!enemyOpenSquares.contains(location)) {
			move(enemyOpenSquares);
		}

		attack();
	}

	private void move(Set<Point2D> enemyOpenSquares) {
		ShortestPath<Point2D> shortestPath = ShortestPathBuilder.build(new ActorMapDescription());
		Map<Point2D, Integer> distanceToEnemyOpenSquares = enemyOpenSquares.stream()
				.filter(point -> shortestPath.stepTo(point) != null)
				.collect(Collectors.toMap(point -> point, shortestPath::distanceTo));
		if (distanceToEnemyOpenSquares.isEmpty()) return;

		int closestDistance = distanceToEnemyOpenSquares.values().stream().min(Integer::compare).orElseThrow();
		Point2D closestPoint = distanceToEnemyOpenSquares.keySet().stream()
				.filter(point -> distanceToEnemyOpenSquares.get(point) == closestDistance)
				.min(PointComparator.INSTANCE)
				.orElseThrow();

		List<Point2D> neighbours = maze.getNeighbours(location);
		while(!neighbours.contains(closestPoint)) closestPoint = shortestPath.stepTo(closestPoint);
		location = closestPoint;
	}

	private void attack() {
		SortedSet<Actor> enemiesToAttack = maze.getNeighbours(location).stream()
				.map(maze::getActorAt)
				.filter(Objects::nonNull)
				.filter(actor -> actor.getType() != type)
				.collect(Collectors.toCollection(ActorComparatorUsingHitPoints::newSortedSet));
		if (enemiesToAttack.isEmpty()) return;

		enemiesToAttack.first().hitPoints -= attackPower;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Actor actor = (Actor) o;
		return id.equals(actor.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return String.format("%s at %s", type, location);
	}

	private class ActorMapDescription implements MapDescription<Point2D> {
		@Override
		public Point2D getOrigin() {
			return location;
		}

		@Override
		public List<Point2D> getNeighbours(Point2D point) {
			Actor actorAtPosition = maze.getActorAt(point);
			if (actorAtPosition != null && actorAtPosition != Actor.this) return Collections.emptyList();

			return maze.getNeighbours(point);
		}

		@Override
		public int getDistance(Point2D from, Point2D to) {
			return 1;
		}
	}
}
