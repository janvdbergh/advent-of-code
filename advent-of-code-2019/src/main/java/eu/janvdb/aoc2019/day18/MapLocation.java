package eu.janvdb.aoc2019.day18;

import java.util.Objects;

public class MapLocation {
	private final Type type;
	private final char symbol;

	public MapLocation(Type type, char symbol) {
		this.type = type;
		this.symbol = symbol;
	}

	public Type getType() {
		return type;
	}

	public char getSymbol() {
		return symbol;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		MapLocation that = (MapLocation) o;
		return symbol == that.symbol && type == that.type;
	}

	@Override
	public int hashCode() {
		return Objects.hash(type, symbol);
	}

	@Override
	public String toString() {
		return String.format("%s %s", type.name(), symbol);
	}

	public enum Type {
		START, KEY, DOOR
	}
}
