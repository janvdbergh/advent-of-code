package eu.janvdb.aoc2019.day19;

import java.util.function.Supplier;

import eu.janvdb.aoc2019.common.Computer;
import eu.janvdb.util.Holder;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.Iterator;

public class Day19 {

	private static final long[] PROGRAM = {
			109, 424, 203, 1, 21102, 11, 1, 0, 1105, 1, 282, 21101, 18, 0, 0, 1106, 0, 259, 2101, 0, 1, 221, 203, 1,
			21101, 0, 31, 0, 1105, 1, 282, 21101, 0, 38, 0, 1106, 0, 259, 21001, 23, 0, 2, 21202, 1, 1, 3, 21102, 1, 1,
			1, 21102, 57, 1, 0, 1105, 1, 303, 2101, 0, 1, 222, 21002, 221, 1, 3, 20101, 0, 221, 2, 21101, 259, 0, 1,
			21101, 0, 80, 0, 1105, 1, 225, 21102, 198, 1, 2, 21102, 91, 1, 0, 1106, 0, 303, 1201, 1, 0, 223, 21002, 222,
			1, 4, 21101, 0, 259, 3, 21102, 225, 1, 2, 21102, 225, 1, 1, 21102, 1, 118, 0, 1106, 0, 225, 21001, 222, 0,
			3, 21101, 0, 140, 2, 21101, 133, 0, 0, 1106, 0, 303, 21202, 1, -1, 1, 22001, 223, 1, 1, 21102, 1, 148, 0,
			1106, 0, 259, 2101, 0, 1, 223, 21002, 221, 1, 4, 21002, 222, 1, 3, 21101, 0, 24, 2, 1001, 132, -2, 224,
			1002, 224, 2, 224, 1001, 224, 3, 224, 1002, 132, -1, 132, 1, 224, 132, 224, 21001, 224, 1, 1, 21102, 1,
			195, 0, 106, 0, 108, 20207, 1, 223, 2, 21001, 23, 0, 1, 21102, 1, -1, 3, 21102, 1, 214, 0, 1106, 0, 303,
			22101, 1, 1, 1, 204, 1, 99, 0, 0, 0, 0, 109, 5, 1201, -4, 0, 249, 21202, -3, 1, 1, 22101, 0, -2, 2, 21202,
			-1, 1, 3, 21102, 1, 250, 0, 1105, 1, 225, 22101, 0, 1, -4, 109, -5, 2106, 0, 0, 109, 3, 22107, 0, -2, -1,
			21202, -1, 2, -1, 21201, -1, -1, -1, 22202, -1, -2, -2, 109, -3, 2106, 0, 0, 109, 3, 21207, -2, 0, -1, 1206,
			-1, 294, 104, 0, 99, 22101, 0, -2, -2, 109, -3, 2105, 1, 0, 109, 5, 22207, -3, -4, -1, 1206, -1, 346, 22201,
			-4, -3, -4, 21202, -3, -1, -1, 22201, -4, -1, 2, 21202, 2, -1, -1, 22201, -4, -1, 1, 22102, 1, -2, 3, 21101,
			0, 343, 0, 1105, 1, 303, 1106, 0, 415, 22207, -2, -3, -1, 1206, -1, 387, 22201, -3, -2, -3, 21202, -2, -1,
			-1, 22201, -3, -1, 3, 21202, 3, -1, -1, 22201, -3, -1, 2, 22101, 0, -4, 1, 21102, 1, 384, 0, 1105, 1, 303,
			1106, 0, 415, 21202, -4, -1, -4, 22201, -4, -3, -4, 22202, -3, -2, -2, 22202, -2, -4, -4, 22202, -3, -2, -3,
			21202, -4, -1, -2, 22201, -3, -2, 1, 21201, 1, 0, -4, 109, -5, 2105, 1, 0
	};

	private static final int GRID_SIZE = 50;
	private static final int SIZE = 100;

	public static void main(String[] args) {
		new Day19().run();
	}

	private void run() {
		part1();
		part2();
	}

	private void part1() {
		int sum = 0;
		for (int y = 0; y < GRID_SIZE; y++) {
			for (int x = 0; x < GRID_SIZE; x++) {
				boolean isLit = isLit(x, y);
				System.out.print(isLit ? 'X' : '.');
				sum += isLit ? 1 : 0;
			}
			System.out.println();
		}
		System.out.println(sum);
	}

	private void part2() {
		Tuple2<Integer, Integer> t1 = find(500, 500, 5); // start values based on part 1
		Tuple2<Integer, Integer> t2 = find(t1._1 - 50, t1._2 - 50, 1); // backtrack a bit and go slower
	}

	private Tuple2<Integer, Integer> find(int startX, int startY, int increment) {
		int x = startX;
		int y = startY;

		while (!isLit(x, y) || !isLit(x + SIZE - 1, y) || !isLit(x, y + SIZE - 1)) {
			if (x < y) {
				x += increment;
			} else {
				y += increment;
				x = 0;
			}
		}

		System.out.printf("%d, %d -> %d\n", x, y, 10000 * x + y);
		return Tuple.of(x, y);
	}

	private boolean isLit(int x, int y) {
		Holder<Long> outputHolder = new Holder<>();
		Computer computer = new Computer(PROGRAM, new IterableSupplier(x, y), outputHolder::setValue);
		computer.run();
		return outputHolder.getValue() == 1L;
	}

	private static class IterableSupplier implements Supplier<Long> {
		private final Iterator<Long> iterator;

		public IterableSupplier(long... values) {
			iterator = Iterator.ofAll(values);
		}

		@Override
		public Long get() {
			return iterator.next();
		}
	}
}
