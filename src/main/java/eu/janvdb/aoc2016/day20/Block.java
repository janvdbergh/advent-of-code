package eu.janvdb.aoc2016.day20;

import static java.lang.Math.*;

public class Block implements Comparable<Block> {

	private final long from;
	private final long to;

	public Block(String input) {
		String[] parts = input.split("\\s*-\\s*");
		if (parts.length != 2) {
			throw new IllegalArgumentException(input);
		}

		this.from = Long.parseLong(parts[0]);
		this.to = Long.parseLong(parts[1]);
	}

	public Block(long from, long to) {
		this.from = from;
		this.to = to;
	}

	public boolean canBeCombined(Block o) {
		if (from <= o.from) {
			if (o.from <= to + 1) return true;
		} else {
			if (from <= o.to + 1) return true;
		}

		return false;
	}

	public Block combine(Block o) {
		if (!canBeCombined(o)) {
			throw new IllegalArgumentException();
		}

		return new Block(min(from, o.from), max(to, o.to));
	}

	public long getSize() {
		return to - from + 1;
	}

	public boolean contains(long number) {
		return from <= number && number <= to;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Block block = (Block) o;
		return from == block.from && to == block.to;
	}

	@Override
	public int hashCode() {
		int result = (int) (from ^ (from >>> 32));
		result = 31 * result + (int) (to ^ (to >>> 32));
		return result;
	}

	@Override
	public int compareTo(Block o) {
		int compare1 = compare(from, o.from);
		return compare1 != 0 ? compare1 : compare(to, o.to);
	}

	private int compare(long a, long b) {
		return a - b < 0 ? -1 : a - b > 0 ? 1 : 0;
	}

	@Override
	public String toString() {
		return "Block{" +
				"from=" + from +
				", to=" + to +
				'}';
	}
}
