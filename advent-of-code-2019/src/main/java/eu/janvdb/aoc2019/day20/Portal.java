package eu.janvdb.aoc2019.day20;

import eu.janvdb.aocutil.java.Point2D;

import java.util.Objects;

public class Portal {

	private final Point2D coordinate;
	private final String label;
	private final Type type;

	public Portal(Type type, String label, Point2D coordinate) {
		this.coordinate = coordinate;
		this.label = label;
		this.type = type;
	}

	public Portal(Type type, String label) {
		this.coordinate = null;
		this.label = label;
		this.type = type;
	}

	public Point2D getCoordinate() {
		return coordinate;
	}

	public String getLabel() {
		return label;
	}

	public boolean isInner() {
		return type == Type.INNER;
	}

	public boolean isOuter() {
		return type == Type.OUTER;
	}

	public boolean isStart() {
		return type == Type.START;
	}

	public boolean isEnd() {
		return type == Type.END;
	}

	public Type getType() {
		return type;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Portal portal = (Portal) o;
		return label.equals(portal.label) && type == portal.type;
	}

	@Override
	public int hashCode() {
		return Objects.hash(label, type);
	}

	@Override
	public String toString() {
		return String.format("%s %s", type, label);
	}

	public enum Type {
		INNER, OUTER, START, END
	}
}
