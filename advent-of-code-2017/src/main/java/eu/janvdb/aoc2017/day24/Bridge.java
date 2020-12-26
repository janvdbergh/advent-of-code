package eu.janvdb.aoc2017.day24;

class Bridge implements Comparable<Bridge> {

	private final int length;
	private final int strength;

	static Bridge empty() {
		return new Bridge();
	}

	Bridge append(Component component) {
		return new Bridge(this, component);
	}

	private Bridge() {
		length = 0;
		strength = 0;
	}

	private Bridge(Bridge bridge, Component extra) {
		length = bridge.length + 1;
		strength = bridge.strength + extra.getStrength();
	}

	int getLength() {
		return length;
	}

	int getStrength() {
		return strength;
	}

	@Override
	public int compareTo(Bridge o) {
		if (length > o.length) return -1;
		if (length < o.length) return 1;
		return Integer.compare(o.strength, strength);
	}

	@Override
	public String toString() {
		return "Bridge{" +
				"length=" + length +
				", strength=" + strength +
				'}';
	}
}
