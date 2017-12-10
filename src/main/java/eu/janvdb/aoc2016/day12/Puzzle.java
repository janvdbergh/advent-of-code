package eu.janvdb.aoc2016.day12;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Stream;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Puzzle {

	private static final List<Tuple2<Pattern, BiConsumer<State, Matcher>>> INSTRUCTIONS = List.of(
			Tuple.of(Pattern.compile("cpy ([abcd]) ([abcd])"), Puzzle::cpyRegister),
			Tuple.of(Pattern.compile("cpy ([+-]?\\d+) ([abcd])"), Puzzle::cpyValue),
			Tuple.of(Pattern.compile("inc ([abcd])"), Puzzle::inc),
			Tuple.of(Pattern.compile("dec ([abcd])"), Puzzle::dec),
			Tuple.of(Pattern.compile("jnz ([abcd]) ([+-]?\\d+)"), Puzzle::jnzRegister),
			Tuple.of(Pattern.compile("jnz ([+-]?\\d+) ([+-]?\\d+)"), Puzzle::jnzValue)
	);

	private static void cpyRegister(State state, Matcher matcher) {
		int register1 = getRegisterIndex(matcher, 1);
		int register2 = getRegisterIndex(matcher, 2);
		state.registers[register2] = state.registers[register1];

		state.pc++;
	}

	private static void cpyValue(State state, Matcher matcher) {
		int value = getNumber(matcher, 1);
		int register = getRegisterIndex(matcher, 2);
		state.registers[register] = value;

		state.pc++;
	}

	private static void inc(State state, Matcher matcher) {
		int register = getRegisterIndex(matcher, 1);
		state.registers[register]++;

		state.pc++;
	}

	private static void dec(State state, Matcher matcher) {
		int register = getRegisterIndex(matcher, 1);
		state.registers[register]--;

		state.pc++;
	}

	private static void jnzRegister(State state, Matcher matcher) {
		int register = getRegisterIndex(matcher, 1);
		int offset = getNumber(matcher, 2);

		if (state.registers[register] != 0) {
			state.pc += offset;
		} else {
			state.pc++;
		}
	}

	private static void jnzValue(State state, Matcher matcher) {
		int value = getNumber(matcher, 1);
		int offset = getNumber(matcher, 2);

		if (value != 0) {
			state.pc += offset;
		} else {
			state.pc++;
		}
	}

	private static int getRegisterIndex(Matcher matcher, int argumentNumber) {
		return matcher.group(argumentNumber).charAt(0) - 'a';
	}

	private static int getNumber(Matcher matcher, int argumentNumber) {
		return Integer.parseInt(matcher.group(argumentNumber));
	}

	public static void main(String[] args) throws IOException {
		new Puzzle().execute();
	}

	private void execute() throws IOException {
		List<Consumer<State>> program = Stream.ofAll(IOUtils.readLines(getClass().getResourceAsStream("code.txt"), "UTF-8"))
				.map(this::assemble)
				.toList();

		State state = new State();
		while (state.pc < program.size()) {
			program.get(state.pc).accept(state);
		}
		System.out.println(state);
	}

	private Consumer<State> assemble(String line) {
		for (Tuple2<Pattern, BiConsumer<State, Matcher>> instruction : INSTRUCTIONS) {
			Matcher matcher = instruction._1().matcher(line);
			if (matcher.matches()) {
				return state -> instruction._2().accept(state, matcher);
			}
		}

		throw new IllegalArgumentException(line);
	}

	private static class State {
		int[] registers = new int[4];
		int pc;

		@Override
		public String toString() {
			return "State{" +
					"pc=" + pc +
					", registers=" + Arrays.toString(registers) +
					'}';
		}
	}
}
