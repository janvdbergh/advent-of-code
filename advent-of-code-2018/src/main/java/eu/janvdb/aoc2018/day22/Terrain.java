package eu.janvdb.aoc2018.day22;

import eu.janvdb.aocutil.java.Point2D;

public class Terrain {

	private final Point2D location;
	private final TerrainType type;

	public Terrain(Point2D location) {
		this.location = location;
		this.type = TerrainType.getTerrainType(location);
	}

	public TerrainType getType() {
		return type;
	}
}
