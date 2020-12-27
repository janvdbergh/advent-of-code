package eu.janvdb.aoc2018.day22;

import eu.janvdb.aocutil.java.Point2D;

import java.util.Objects;

public class State {
	private final Tool tool;
	private final Point2D location;
	private final TerrainType terrainType;

	public State(Tool tool, Point2D location, TerrainType terrainType) {
		this.tool = tool;
		this.location = location;
		this.terrainType = terrainType;
	}

	public Tool getTool() {
		return tool;
	}

	public Point2D getLocation() {
		return location;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		State state = (State) o;
		return tool == state.tool && location.equals(state.location);
	}

	@Override
	public int hashCode() {
		return Objects.hash(tool, location);
	}

	@Override
	public String toString() {
		return String.format("At %s: %s with %s", location, terrainType, tool);
	}

	public boolean isValid() {
		if (terrainType == null) return false;
		return switch (terrainType) {
			case ROCKY -> tool != Tool.NONE;
			case WET -> tool != Tool.TORCH;
			case NARROW -> tool != Tool.CLIMBING_GEAR;
		};
	}
}
