package eu.janvdb.aoc2018.day22;

import eu.janvdb.aocutil.java.Point2D;

public enum TerrainType {
	ROCKY(0), WET(1), NARROW(2);

	private final int riskLevel;

	TerrainType(int riskLevel) {
		this.riskLevel = riskLevel;
	}

	public int getRiskLevel() {
		return riskLevel;
	}

	public static TerrainType getTerrainType(Point2D location) {
		return switch ((ErosionLevelCalculator.getErosionLevel(location) % 3)) {
			case 0 -> TerrainType.ROCKY;
			case 1 -> TerrainType.WET;
			case 2 -> TerrainType.NARROW;
			default -> null;
		};
	}


}