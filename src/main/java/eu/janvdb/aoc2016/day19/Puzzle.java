package eu.janvdb.aoc2016.day19;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.collections4.list.TreeList;

import javaslang.Tuple2;
import javaslang.collection.Queue;

public class Puzzle {

//		private static final int NUMBER_OF_ELVES = 5;
	private static final int NUMBER_OF_ELVES = 3_012_210;

	public static void main(String[] args) {
//		new Puzzle().execute1();
		new Puzzle().execute2();
	}

	private void execute1() {
		Queue<Integer> elves = Queue.range(1, NUMBER_OF_ELVES + 1);
		Queue<Integer> presentsPerElf = elves.map(x -> 1);

		while (elves.size() != 1) {
			Tuple2<Integer, Queue<Integer>> item1, item2;

			item1 = presentsPerElf.dequeue();
			item2 = item1._2.dequeue();
			presentsPerElf = item2._2.enqueue(item1._1 + item2._1);

			item1 = elves.dequeue();
			item2 = item1._2.dequeue();
			elves = item2._2.enqueue(item1._1);
		}

		System.out.println(elves);
		System.out.println(presentsPerElf);
	}

	private void execute2() {
		List<Integer> elves = IntStream.range(1, NUMBER_OF_ELVES+1).mapToObj(x -> x).collect(Collectors.toCollection(TreeList::new));

		while (elves.size() != 1) {
			int otherIndex = elves.size() / 2;

			elves.remove(otherIndex);
			Integer removedElf = elves.remove(0);
			elves.add(removedElf);

			if (elves.size() % 10000 == 0) {
				System.out.println(System.currentTimeMillis()/1000%3600 + ": " + elves.size());
			}
		}

		System.out.println(elves);
	}
}
