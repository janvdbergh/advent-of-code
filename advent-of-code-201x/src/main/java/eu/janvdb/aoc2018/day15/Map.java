package eu.janvdb.aoc2018.day15;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

class Map {

	private final int width, heigth;
	private final boolean[] walls;
	private final SortedSet<Actor> actors;

	Map(List<String> description) {
		this.heigth = description.size();
		this.width = description.stream().mapToInt(String::length).max().orElseThrow();
		this.walls = new boolean[width * heigth];
		this.actors = new TreeSet<>();

		for (int y = 0; y < heigth; y++) {
			String line = description.get(y);
			for (int x = 0; x < line.length(); x++) {
				if (line.charAt(x) == '#') walls[coord(x, y)] = true;
				if (line.charAt(x) == 'E') actors.add(new Actor(x, y, Actor.Type.ELF));
				if (line.charAt(x) == 'G') actors.add(new Actor(x, y, Actor.Type.GOBLIN));
			}
		}
	}

	void print() {
		for (int y = 0; y < heigth; y++) {
			for (int x = 0; x < width; x++) {
				if (isWall(x, y))
					System.out.print('#');
				else if (isActorOfType(x, y, Actor.Type.ELF))
					System.out.print('E');
				else if (isActorOfType(x, y, Actor.Type.GOBLIN))
					System.out.print('G');
				else
					System.out.print('.');
			}
			System.out.println();
		}
	}

	void executeTurn() {
		actors.stream()
				.filter(actor -> actor.getHitPoints() > 0)
				.forEach(this::moveAndAttack);
	}

	private void moveAndAttack(Actor actor) {

	}

	private boolean isWall(int x, int y) {
		return walls[coord(x, y)];
	}

	private boolean isActorOfType(int x, int y, Actor.Type type) {
		return actors.stream()
				.anyMatch(actor -> actor.getX() == x && actor.getY() == y && actor.getType() == type);
	}

	private int coord(int x, int y) {
		return x + y * width;
	}
}
