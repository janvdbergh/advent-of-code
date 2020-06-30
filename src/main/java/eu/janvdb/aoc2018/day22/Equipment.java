package eu.janvdb.aoc2018.day22;

enum Equipment {

	TORCH, CLIMBING_GEAR, NEITHER;


	public boolean canAccess(Terrain terrain) {
		switch (terrain) {
			case ROCKY:
				return this == TORCH || this == CLIMBING_GEAR;
			case WET:
				return this == CLIMBING_GEAR || this == NEITHER;
			case NARROW:
				return this == NEITHER || this == TORCH;
		}
		throw new IllegalStateException();
	}

}
