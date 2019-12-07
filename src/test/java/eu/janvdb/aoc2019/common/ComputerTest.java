package eu.janvdb.aoc2019.common;

import io.reactivex.Observable;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ComputerTest {

	@Test
	public void test1() {
		int[] program = {3, 9, 8, 9, 10, 9, 4, 9, 99, -1, 8};
		runComputerTest(program, List.of(8), List.of(1));
		runComputerTest(program, List.of(7), List.of(0));
	}

	@Test
	public void test2() {
		int[] program = {3, 9, 7, 9, 10, 9, 4, 9, 99, -1, 8};
		runComputerTest(program, List.of(7), List.of(1));
		runComputerTest(program, List.of(8), List.of(0));
	}

	@Test
	public void test3() {
		int[] program = {3, 3, 1108, -1, 8, 3, 4, 3, 99};
		runComputerTest(program, List.of(8), List.of(1));
		runComputerTest(program, List.of(7), List.of(0));
	}

	@Test
	public void test4() {
		int[] program = {3, 3, 1107, -1, 8, 3, 4, 3, 99};
		runComputerTest(program, List.of(7), List.of(1));
		runComputerTest(program, List.of(8), List.of(0));
	}

	@Test
	public void test5() {
		int[] program = {3, 12, 6, 12, 15, 1, 13, 14, 13, 4, 13, 99, -1, 0, 1, 9};
		runComputerTest(program, List.of(0), List.of(0));
		runComputerTest(program, List.of(10), List.of(1));
	}

	@Test
	public void test6() {
		int[] program = {3, 3, 1105, -1, 9, 1101, 0, 0, 12, 4, 12, 99, 1};
		runComputerTest(program, List.of(0), List.of(0));
		runComputerTest(program, List.of(10), List.of(1));
	}

	@Test
	public void test7() {
		int[] program = {
				3, 21, 1008, 21, 8, 20, 1005, 20, 22, 107, 8, 21, 20, 1006, 20, 31,
				1106, 0, 36, 98, 0, 0, 1002, 21, 125, 20, 4, 20, 1105, 1, 46, 104,
				999, 1105, 1, 46, 1101, 1000, 1, 20, 4, 20, 1105, 1, 46, 98, 99
		};
		runComputerTest(program, List.of(6), List.of(999));
		runComputerTest(program, List.of(8), List.of(1000));
		runComputerTest(program, List.of(12), List.of(1001));
	}

	private void runComputerTest(int[] program, List<Integer> inputs, List<Integer> expectedOutputs) {
		List<Integer> results = new ArrayList<>();
		new Computer(program, Observable.fromIterable(inputs), results::add).run();
		assertThat(results).isEqualTo(expectedOutputs);
	}

}