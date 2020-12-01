package eu.janvdb.aoc2018.day22;

import java.util.Objects;
import java.util.Optional;

class Location {

	private static final int MINUTES_BETWEEN_LOCATIONS = 1;
	private static final int MINUTES_TO_SWITCH_EQUIPMENT = 7;
	private static final int NO_ROUTE = Integer.MAX_VALUE / 2;

	private final int x, y;
	private final Terrain terrain;
	private final Equipment equipment;

	private int bestDistance = NO_ROUTE;

	Location(int x, int y, Terrain terrain, Equipment equipment) {
		this.x = x;
		this.y = y;
		this.terrain = terrain;
		this.equipment = equipment;
	}

	Optional<Integer> distance(Location other) {
		if (this.isNextTo(other) && other.equipment == this.equipment && equipment.canAccess(other.terrain)) return Optional.of(MINUTES_BETWEEN_LOCATIONS);
		if (this.isSameSpotAs(other) && other.equipment != this.equipment) return Optional.of(MINUTES_TO_SWITCH_EQUIPMENT);
		return Optional.empty();
	}

	private boolean isNextTo(Location other) {
		int dx = Math.abs(other.x - x);
		int dy = Math.abs(other.y - y);
		return (dx == 1 && dy == 0) || (dx == 0 && dy == 1);
	}

	private boolean isSameSpotAs(Location other) {
		return other.x == x && other.y == y;
	}

	int getX() {
		return x;
	}

	int getY() {
		return y;
	}

	Equipment getEquipment() {
		return equipment;
	}

	int getBestDistance() {
		return bestDistance;
	}

	void setBestDistance(int bestDistance) {
		this.bestDistance = bestDistance;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Location location = (Location) o;
		return x == location.x &&
				y == location.y &&
				equipment == location.equipment;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y, equipment);
	}
}
