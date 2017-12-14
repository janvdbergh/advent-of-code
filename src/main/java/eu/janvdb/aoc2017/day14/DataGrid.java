package eu.janvdb.aoc2017.day14;

import eu.janvdb.aoc2017.day10.Knotter;
import io.vavr.Tuple;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.collection.Stream;
import io.vavr.collection.Vector;


class DataGrid {

	static final int SIZE = 128;
	static final int NOT_IN_GROUP = -1;

	private static final Map<Character, String> BITS_PER_HEX_MAP = HashMap.ofEntries(
			Tuple.of('0', "0000"), Tuple.of('1', "0001"), Tuple.of('2', "0010"), Tuple.of('3', "0011"),
			Tuple.of('4', "0100"), Tuple.of('5', "0101"), Tuple.of('6', "0110"), Tuple.of('7', "0111"),
			Tuple.of('8', "1000"), Tuple.of('9', "1001"), Tuple.of('A', "1010"), Tuple.of('B', "1011"),
			Tuple.of('C', "1100"), Tuple.of('D', "1101"), Tuple.of('E', "1110"), Tuple.of('F', "1111")
	);

	private final Vector<Integer> bits;

	DataGrid(String startHash) {
		Knotter knotter = new Knotter();
		bits = Stream.range(0, 128)
				.map(number -> startHash + "-" + number)
				.map(knotter::knotString)
				.flatMap(this::toBinaryString)
				.map(ch -> ch == '1' ? NOT_IN_GROUP : 0)
				.toVector();

		if (bits.size() != SIZE * SIZE) {
			throw new IllegalStateException();
		}
	}

	private DataGrid(Vector<Integer> bits) {
		this.bits = bits;
	}

	private Stream<Character> toBinaryString(String value) {
		return Stream.ofAll(value.toCharArray())
				.map(Character::toUpperCase)
				.map(ch -> BITS_PER_HEX_MAP.get(ch).getOrElseThrow(IllegalArgumentException::new))
				.flatMap(s -> Stream.ofAll(s.toCharArray()));
	}

	int countSetBits() {
		return bits.count(bit -> bit != 0);
	}

	DataGrid markGroup(int row, int column, int group) {
		if (row < 0 || row >= SIZE || column < 0 || column >= SIZE || getBit(row, column) != NOT_IN_GROUP) {
			return this;
		}

		return setBit(row, column, group)
				.markGroup(row - 1, column, group)
				.markGroup(row + 1, column, group)
				.markGroup(row, column - 1, group)
				.markGroup(row, column + 1, group);
	}

	int getBit(int row, int column) {
		return bits.get(index(row, column));
	}

	private DataGrid setBit(int row, int column, int group) {
		Vector<Integer> newBits = bits.update(index(row, column), group);
		return new DataGrid(newBits);
	}

	private int index(int row, int column) {
		return row * SIZE + column;
	}

	int getMaxGroup() {
		return bits.max().getOrElse(0);
	}
}

