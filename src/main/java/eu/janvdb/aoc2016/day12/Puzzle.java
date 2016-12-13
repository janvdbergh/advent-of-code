package eu.janvdb.aoc2016.day12;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;

import eu.janvdb.util.Pair;

public class Puzzle {

	private static final List<Pair<Pattern, BiConsumer<State, Matcher>>> INSTRUCTIONS = Arrays.asList(
			new Pair<Pattern, BiConsumer<State, Matcher>>(Pattern.compile("cpy ([abcd]) ([abcd])"), Puzzle::cpyRegister),
			new Pair<Pattern, BiConsumer<State, Matcher>>(Pattern.compile("cpy ([+-]?\\d+) ([abcd])"), Puzzle::cpyValue),
			new Pair<Pattern, BiConsumer<State, Matcher>>(Pattern.compile("inc ([abcd])"), Puzzle::inc),
			new Pair<Pattern, BiConsumer<State, Matcher>>(Pattern.compile("dec ([abcd])"), Puzzle::dec),
			new Pair<Pattern, BiConsumer<State, Matcher>>(Pattern.compile("jnz ([abcd]) ([+-]?\\d+)"), Puzzle::jnzRegister),
			new Pair<Pattern, BiConsumer<State, Matcher>>(Pattern.compile("jnz ([+-]?\\d+) ([+-]?\\d+)"), Puzzle::jnzValue)
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
		List<Consumer<State>> program = IOUtils.readLines(getClass().getResourceAsStream("code.txt"), Charset.forName("UTF-8")).stream()
				.map(this::assemble)
				.collect(Collectors.toList());

		State state = new State();
		while (state.pc < program.size()) {
			program.get(state.pc).accept(state);
		}
		System.out.println(state);
	}

	private Consumer<State> assemble(String line) {
		for (Pair<Pattern, BiConsumer<State, Matcher>> instruction : INSTRUCTIONS) {
			Matcher matcher = instruction.getV1().matcher(line);
			if (matcher.matches()) {
				return state -> instruction.getV2().accept(state, matcher);
			}
		}

		throw new IllegalArgumentException(line);
	}

	private static class State {
		public int[] registers = new int[4];
		public int pc;

		@Override
		public String toString() {
			return "State{" +
					"pc=" + pc +
					", registers=" + Arrays.toString(registers) +
					'}';
		}
	}
}
