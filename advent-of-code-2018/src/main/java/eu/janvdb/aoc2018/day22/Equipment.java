package eu.janvdb.aoc2018.day22;

enum Equipment {

	TORCH, CLIMBING_GEAR, NEITHER;

	public boolean canAccess(Terrain terrain) {
		return switch (terrain) {
			case ROCKY -> this == TORCH || this == CLIMBING_GEAR;
			case WET -> this == CLIMBING_GEAR || this == NEITHER;
			case NARROW -> this == NEITHER || this == TORCH;
		};
	}

}
