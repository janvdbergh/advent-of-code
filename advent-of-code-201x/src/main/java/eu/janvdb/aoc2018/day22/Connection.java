package eu.janvdb.aoc2018.day22;

class Connection {

	private final Location location1, location2;
	private final int distance;

	Connection(Location location1, Location location2, int distance) {
		this.location1 = location1;
		this.location2 = location2;
		this.distance = distance;
	}

	Location getLocation2() {
		return location2;
	}

	int getDistance() {
		return distance;
	}
}
