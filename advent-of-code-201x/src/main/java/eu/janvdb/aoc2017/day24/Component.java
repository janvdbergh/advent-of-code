package eu.janvdb.aoc2017.day24;

public class Component {

	private final int port1, port2;

	Component(String description) {
		String[] parts = description.split("/");
		port1 = Integer.parseInt(parts[0]);
		port2 = Integer.parseInt(parts[1]);

	}

	int getPort1() {
		return port1;
	}

	int getPort2() {
		return port2;
	}

	int getStrength() {
		return port1 + port2;
	}

	boolean matches(int port) {
		return port1 == port || port2 == port;
	}

	int getRemainingPort(int port) {
		return port == port1 ? port2 : port1;
	}

	@Override
	public String toString() {
		return "(" + port1 + "-" + port2 + ')';
	}
}
