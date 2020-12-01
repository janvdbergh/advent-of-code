package eu.janvdb.aoc2019.common;

import io.reactivex.Observable;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class ComputerTest {

	@Test
	public void test1() {
		long[] program = {3, 9, 8, 9, 10, 9, 4, 9, 99, -1, 8};
		runComputerTest(program, List.of(8L), List.of(1L));
		runComputerTest(program, List.of(7L), List.of(0L));
	}

	@Test
	public void test2() {
		long[] program = {3, 9, 7, 9, 10, 9, 4, 9, 99, -1, 8};
		runComputerTest(program, List.of(7L), List.of(1L));
		runComputerTest(program, List.of(8L), List.of(0L));
	}

	@Test
	public void test3() {
		long[] program = {3, 3, 1108, -1, 8, 3, 4, 3, 99};
		runComputerTest(program, List.of(8L), List.of(1L));
		runComputerTest(program, List.of(7L), List.of(0L));
	}

	@Test
	public void test4() {
		long[] program = {3, 3, 1107, -1, 8, 3, 4, 3, 99};
		runComputerTest(program, List.of(7L), List.of(1L));
		runComputerTest(program, List.of(8L), List.of(0L));
	}

	@Test
	public void test5() {
		long[] program = {3, 12, 6, 12, 15, 1, 13, 14, 13, 4, 13, 99, -1, 0, 1, 9};
		runComputerTest(program, List.of(0L), List.of(0L));
		runComputerTest(program, List.of(10L), List.of(1L));
	}

	@Test
	public void test6() {
		long[] program = {3, 3, 1105, -1, 9, 1101, 0, 0, 12, 4, 12, 99, 1};
		runComputerTest(program, List.of(0L), List.of(0L));
		runComputerTest(program, List.of(10L), List.of(1L));
	}

	@Test
	public void test7() {
		long[] program = {
				3, 21, 1008, 21, 8, 20, 1005, 20, 22, 107, 8, 21, 20, 1006, 20, 31,
				1106, 0, 36, 98, 0, 0, 1002, 21, 125, 20, 4, 20, 1105, 1, 46, 104,
				999, 1105, 1, 46, 1101, 1000, 1, 20, 4, 20, 1105, 1, 46, 98, 99
		};
		runComputerTest(program, List.of(6L), List.of(999L));
		runComputerTest(program, List.of(8L), List.of(1000L));
		runComputerTest(program, List.of(12L), List.of(1001L));
	}

	@Test
	public void test8() {
		long[] program = {109, 1, 204, -1, 1001, 100, 1, 100, 1008, 100, 16, 101, 1006, 101, 0, 99};
		List<Long> expectedOutput = Arrays.stream(program).boxed().collect(Collectors.toList());
		runComputerTest(program, List.of(), expectedOutput);
	}

	@Test
	public void test9() {
		long[] program = {1102, 34915192, 34915192, 7, 4, 7, 99, 0};
		runComputerTest(program, List.of(), List.of(1219070632396864L));
	}

	@Test
	public void test10() {
		long[] program = {104, 1125899906842624L, 99};
		runComputerTest(program, List.of(), List.of(1125899906842624L));
	}

	private void runComputerTest(long[] program, List<Long> inputs, List<Long> expectedOutputs) {
		List<Long> results = new ArrayList<>();

		ReactiveComputer computer = new ReactiveComputer(program);
		computer.setInput(Observable.fromIterable(inputs));
		computer.getOutput().subscribe(results::add);

		computer.start();
		computer.join();

		assertThat(results).isEqualTo(expectedOutputs);
	}
}