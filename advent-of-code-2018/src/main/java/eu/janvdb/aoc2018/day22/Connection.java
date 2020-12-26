package eu.janvdb.aoc2018.day22;

class Connection {

	private final Location to;
	private final int distance;

	Connection(Location to, int distance) {
		this.to = to;
		this.distance = distance;
	}

	Location getTo() {
		return to;
	}

	int getDistance() {
		return distance;
	}
}
