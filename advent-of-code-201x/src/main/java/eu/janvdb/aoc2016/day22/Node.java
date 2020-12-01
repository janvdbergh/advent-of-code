package eu.janvdb.aoc2016.day22;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Node {

	private static final Pattern PATTERN = Pattern.compile("/dev/grid/node-x(\\d+)-y(\\d+)\\s+(\\d+)T\\s+(\\d+)T\\s+(\\d+)T\\s+(\\d+)%");

	private final Location location;
	private final int size, available, use;

	public Node(String description) {
		Matcher matcher = PATTERN.matcher(description);
		if (!matcher.matches()) {
			throw new IllegalArgumentException("line");
		}

		int x = Integer.parseInt(matcher.group(1));
		int y = Integer.parseInt(matcher.group(2));
		location = new Location(x, y);
		size = Integer.parseInt(matcher.group(3));
		use = Integer.parseInt(matcher.group(4));
		available = Integer.parseInt(matcher.group(5));
	}

	private Node(Location location, int size, int available, int use) {
		this.location = location;
		this.size = size;
		this.available = available;
		this.use = use;
	}

	public Location getLocation() {
		return location;
	}

	public int getAvailable() {
		return available;
	}

	public int getUse() {
		return use;
	}

	public boolean isViablePairWith(Node other) {
		return other != this && this.use != 0 && other.available >= this.use;
	}

	public boolean canTakeDataFrom(Node other) {
		boolean viablePairWith = other.isViablePairWith(this);
		boolean nextTo = isNextTo(other);
		return viablePairWith && nextTo;
	}

	private boolean isNextTo(Node other) {
		return location.isNextTo(other.location);
	}

	public Node withExtraData(int extraUse) {
		return new Node(location, size, available - extraUse, use + extraUse);
	}

	public Node emptied() {
		return new Node(location, size, available + use, 0);
	}

	@Override
	public String toString() {
		return "Node{" +
				"location=" + location +
				", available=" + available +
				", use=" + use +
				'}';
	}

	public String toDisplayString() {
		char ch = size > 200 ? '#' : (use==0 ? '_' : '.');
		if (location.getX() == 0 && location.getY() == 0) {
			return "(" + ch + ')';
		}
		return " " + ch + " ";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Node node = (Node) o;
		return use == node.use && location.equals(node.location);
	}

	@Override
	public int hashCode() {
		int result = location.hashCode();
		result = 31 * result + use;
		return result;
	}

	public static Node empty(int x, int y) {
		return new Node(new Location(x, y), 0, 0, 0);
	}
}
