package eu.janvdb.aoc2018.day15;

class Actor implements Comparable<Actor> {

	enum Type {ELF, GOBLIN}

	private final int x, y;
	private final Type type;
	private int hitPoints;

	Actor(int x, int y, Type type) {
		this.x = x;
		this.y = y;
		this.type = type;

		this.hitPoints = 200;
	}

	@Override
	public int compareTo(Actor o) {
		return x != o.x ? x - o.x : y - o.y;
	}

	int getX() {
		return x;
	}

	int getY() {
		return y;
	}

	Type getType() {
		return type;
	}

	int getHitPoints() {
		return hitPoints;
	}
}
