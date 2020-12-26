package eu.janvdb.aoc2019.day7;

import eu.janvdb.aoc2019.common.ReactiveComputer;
import eu.janvdb.aoc2019.common.ThreadedComputer;
import eu.janvdb.aocutil.java.Holder;
import eu.janvdb.aocutil.java.Permutations;
import io.reactivex.Observable;
import io.vavr.collection.List;

public class Day7 {

	private final long[] TEST_PROGRAM = {
			3, 23, 3, 24, 1002, 24, 10, 24, 1002, 23, -1, 23,
			101, 5, 23, 23, 1, 24, 23, 23, 4, 23, 99, 0, 0
	};

	private final long[] PROGRAM = {
			3, 8, 1001, 8, 10, 8, 105, 1, 0, 0, 21, 38, 63, 88, 97, 118, 199, 280, 361, 442, 99999, 3, 9, 1002, 9, 3, 9,
			101, 2, 9, 9, 1002, 9, 4, 9, 4, 9, 99, 3, 9, 101, 3, 9, 9, 102, 5, 9, 9, 101, 3, 9, 9, 1002, 9, 3, 9, 101,
			3, 9, 9, 4, 9, 99, 3, 9, 1002, 9, 2, 9, 1001, 9, 3, 9, 102, 3, 9, 9, 101, 2, 9, 9, 1002, 9, 4, 9, 4, 9, 99,
			3, 9, 102, 2, 9, 9, 4, 9, 99, 3, 9, 102, 4, 9, 9, 101, 5, 9, 9, 102, 2, 9, 9, 101, 5, 9, 9, 4, 9, 99, 3, 9,
			1002, 9, 2, 9, 4, 9, 3, 9, 101, 1, 9, 9, 4, 9, 3, 9, 102, 2, 9, 9, 4, 9, 3, 9, 101, 1, 9, 9, 4, 9, 3, 9,
			101, 2, 9, 9, 4, 9, 3, 9, 1001, 9, 2, 9, 4, 9, 3, 9, 102, 2, 9, 9, 4, 9, 3, 9, 102, 2, 9, 9, 4, 9, 3, 9,
			101, 1, 9, 9, 4, 9, 3, 9, 102, 2, 9, 9, 4, 9, 99, 3, 9, 101, 1, 9, 9, 4, 9, 3, 9, 102, 2, 9, 9, 4, 9, 3, 9,
			1001, 9, 2, 9, 4, 9, 3, 9, 1001, 9, 2, 9, 4, 9, 3, 9, 1001, 9, 1, 9, 4, 9, 3, 9, 1001, 9, 1, 9, 4, 9, 3, 9,
			1001, 9, 2, 9, 4, 9, 3, 9, 102, 2, 9, 9, 4, 9, 3, 9, 1002, 9, 2, 9, 4, 9, 3, 9, 1001, 9, 1, 9, 4, 9, 99, 3,
			9, 1002, 9, 2, 9, 4, 9, 3, 9, 1002, 9, 2, 9, 4, 9, 3, 9, 102, 2, 9, 9, 4, 9, 3, 9, 1002, 9, 2, 9, 4, 9, 3,
			9, 1001, 9, 1, 9, 4, 9, 3, 9, 102, 2, 9, 9, 4, 9, 3, 9, 102, 2, 9, 9, 4, 9, 3, 9, 101, 1, 9, 9, 4, 9, 3, 9,
			102, 2, 9, 9, 4, 9, 3, 9, 102, 2, 9, 9, 4, 9, 99, 3, 9, 102, 2, 9, 9, 4, 9, 3, 9, 101, 1, 9, 9, 4, 9, 3, 9,
			1002, 9, 2, 9, 4, 9, 3, 9, 1002, 9, 2, 9, 4, 9, 3, 9, 102, 2, 9, 9, 4, 9, 3, 9, 1002, 9, 2, 9, 4, 9, 3, 9,
			1001, 9, 2, 9, 4, 9, 3, 9, 101, 2, 9, 9, 4, 9, 3, 9, 1001, 9, 2, 9, 4, 9, 3, 9, 101, 1, 9, 9, 4, 9, 99, 3,
			9, 101, 1, 9, 9, 4, 9, 3, 9, 101, 1, 9, 9, 4, 9, 3, 9, 101, 1, 9, 9, 4, 9, 3, 9, 102, 2, 9, 9, 4, 9, 3, 9,
			1001, 9, 1, 9, 4, 9, 3, 9, 1001, 9, 2, 9, 4, 9, 3, 9, 101, 2, 9, 9, 4, 9, 3, 9, 102, 2, 9, 9, 4, 9, 3, 9,
			1001, 9, 1, 9, 4, 9, 3, 9, 1001, 9, 2, 9, 4, 9, 99
	};

	public static void main(String[] args) {
		new Day7().run();
	}

	private void run() {
		part1();
		part2();
	}

	private void part1() {
		final List<Integer> phaseSettings1 = List.range(0, 5);

		Long result = Permutations.getAllPermutations(phaseSettings1)
				.map(phaseSettings -> getOutput1(phaseSettings, PROGRAM))
				.max().get();

		System.out.println(result);
	}

	private Long getOutput1(List<Integer> phaseSettings, long[] program) {
		Observable<Long> current = Observable.just(0L);
		for (int phaseSetting : phaseSettings) {
			ReactiveComputer computer = new ReactiveComputer(program);
			computer.setInput(Observable.just((long) phaseSetting).mergeWith(current));
			computer.start();
			current = computer.getOutput();
		}

		return current.blockingFirst();
	}

	private void part2() {
		final List<Integer> phaseSettings2 = List.range(5, 10);

		Long result = Permutations.getAllPermutations(phaseSettings2)
				.map(phaseSettings -> getOutput2(phaseSettings, PROGRAM))
				.max().get();

		System.out.println(result);
	}

	private Long getOutput2(List<Integer> phaseSettings, long[] program) {
		List<ReactiveComputer> computers = createComputers(phaseSettings, program);
		connectComputers(phaseSettings, computers);

		Holder<Long> lastValueHolder = new Holder<>();
		computers.last().getOutput().subscribe(lastValueHolder::setValue);

		runComputers(computers);
		return lastValueHolder.getValue();
	}

	private void connectComputers(List<Integer> phaseSettings, List<ReactiveComputer> computers) {
		for (int i = 1; i < computers.length(); i++) {
			ReactiveComputer previousComputer = computers.get(i - 1);
			long phaseSetting = phaseSettings.get(i);
			computers.get(i).setInput(Observable.just(phaseSetting).mergeWith(previousComputer.getOutput()));
		}
		ReactiveComputer lastComputer = computers.get(computers.length() - 1);
		computers.get(0).setInput(Observable.just((long) phaseSettings.get(0), 0L).mergeWith(lastComputer.getOutput()));
	}

	private void runComputers(List<ReactiveComputer> computers) {
		computers.forEach(ThreadedComputer::start);
		computers.forEach(ThreadedComputer::join);
	}

	private List<ReactiveComputer> createComputers(List<Integer> phaseSettings, long[] program) {
		List<ReactiveComputer> computers = List.empty();
		for (int ignored : phaseSettings) {
			computers = computers.append(new ReactiveComputer(program));
		}
		return computers;
	}
}
