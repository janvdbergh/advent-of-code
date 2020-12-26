package eu.janvdb.aoc2017.day12;

import eu.janvdb.aocutil.java.InputReader;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Set;

public class Puzzle {

	private static final String INPUT_FILE = "input.txt";

	public static void main(String[] args) {
		Map<Integer, Program> programsById = part1();

		part2(programsById);
	}

	private static Map<Integer, Program> part1() {
		Map<Integer, Program> programsById = InputReader.readInput(Puzzle.class.getResource(INPUT_FILE))
				.map(Program::new)
				.toMap(Program::getId, program -> program);

		Set<Integer> programsReachableFrom = getProgramsReachableFrom(programsById, 0);
		System.out.println(programsReachableFrom.size());
		System.out.println(programsReachableFrom);
		return programsById;
	}

	private static void part2(Map<Integer, Program> programsById) {
		Map<Integer, Set<Integer>> programReachableFromById = programsById.keySet()
				.toMap(programId -> programId, programId -> getProgramsReachableFrom(programsById, programId));
		Map<Integer, Integer> numberReachableFromById = programReachableFromById
				.map((key, items) -> Tuple.of(key, items.size()));

		Set<Integer> idsLeft = programsById.keySet();
		List<Integer> largestGroups = List.empty();
		while (!idsLeft.isEmpty()) {
			Integer largestOne = idsLeft.toStream()
					.map(programId -> Tuple.of(programId, numberReachableFromById.get(programId).getOrElse(0)))
					.sorted((tuple1, tuple2) -> Integer.compare(tuple2._2, tuple1._2))
					.map(Tuple2::_1)
					.getOrElseThrow(IllegalStateException::new);

			largestGroups = largestGroups.append(largestOne);
			idsLeft = idsLeft.removeAll(programReachableFromById.get(largestOne).getOrElseThrow(IllegalStateException::new));
		}

		System.out.println(largestGroups.size());
		System.out.println(largestGroups);
	}

	private static Set<Integer> getProgramsReachableFrom(Map<Integer, Program> programsById, int programId) {
		List<Integer> toDo = List.of(programId);
		Set<Integer> done = HashSet.empty();
		while (!toDo.isEmpty()) {
			Tuple2<Integer, List<Integer>> popped = toDo.pop2();
			toDo = popped._2;
			Integer item = popped._1;
			done = done.add(item);

			Program program = programsById.get(item).getOrElseThrow(IllegalStateException::new);
			toDo = toDo.appendAll(program.getConnections().removeAll(done));
		}
		return done;
	}
}
