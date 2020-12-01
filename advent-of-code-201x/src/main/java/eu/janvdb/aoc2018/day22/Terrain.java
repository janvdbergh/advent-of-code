package eu.janvdb.aoc2018.day22;

public enum Terrain {

	ROCKY(0),
	WET(1),
	NARROW(2);

	private final int type;

	Terrain(int type) {
		this.type = type;
	}

	static Terrain byType(int type) {
		for (Terrain terrain : Terrain.values()) {
			if (terrain.type == type) return terrain;
		}
		throw new IllegalStateException();
	}
}
