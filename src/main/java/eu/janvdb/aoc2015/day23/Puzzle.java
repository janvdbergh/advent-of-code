package eu.janvdb.aoc2015.day23;

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
			new Pair<Pattern, BiConsumer<State, Matcher>>(Pattern.compile("hlf ([ab])"), Puzzle::hlf),
			new Pair<Pattern, BiConsumer<State, Matcher>>(Pattern.compile("tpl ([ab])"), Puzzle::tpl),
			new Pair<Pattern, BiConsumer<State, Matcher>>(Pattern.compile("inc ([ab])"), Puzzle::inc),
			new Pair<Pattern, BiConsumer<State, Matcher>>(Pattern.compile("jmp ([+-]\\d+)"), Puzzle::jmp),
			new Pair<Pattern, BiConsumer<State, Matcher>>(Pattern.compile("jie ([ab]), ([+-]\\d+)"), Puzzle::jie),
			new Pair<Pattern, BiConsumer<State, Matcher>>(Pattern.compile("jio ([ab]), ([+-]\\d+)"), Puzzle::jio)
	);

	private static void hlf(State state, Matcher matcher) {
		switch (matcher.group(1)) {
			case "a":
				state.a /= 2;
				break;
			case "b":
				state.b /= 2;
				break;
			default:
				throw new IllegalArgumentException();
		}
		state.pc++;
	}

	private static void tpl(State state, Matcher matcher) {
		switch (matcher.group(1)) {
			case "a":
				state.a *= 3;
				break;
			case "b":
				state.b *= 3;
				break;
			default:
				throw new IllegalArgumentException();
		}
		state.pc++;
	}

	private static void inc(State state, Matcher matcher) {
		switch (matcher.group(1)) {
			case "a":
				state.a++;
				break;
			case "b":
				state.b++;
				break;
			default:
				throw new IllegalArgumentException();
		}
		state.pc++;
	}

	private static void jmp(State state, Matcher matcher) {
		state.pc += Integer.parseInt(matcher.group(1));
	}

	private static void jie(State state, Matcher matcher) {
		int jmp = Integer.parseInt(matcher.group(2));
		switch (matcher.group(1)) {
			case "a":
				if (state.a % 2 == 0)
					state.pc += jmp;
				else
					state.pc++;
				break;
			case "b":
				if (state.b % 2 == 0)
					state.pc += jmp;
				else
					state.pc++;
				break;
			default:
				throw new IllegalArgumentException();
		}
	}

	private static void jio(State state, Matcher matcher) {
		int jmp = Integer.parseInt(matcher.group(2));
		switch (matcher.group(1)) {
			case "a":
				if (state.a == 1)
					state.pc += jmp;
				else
					state.pc++;
				break;
			case "b":
				if (state.b == 1)
					state.pc += jmp;
				else
					state.pc++;
				break;
			default:
				throw new IllegalArgumentException();
		}
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
			System.out.println(state);
		}
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
		public int a, b, pc;

		@Override
		public String toString() {
			return "State{" +
					"a=" + a +
					", b=" + b +
					", pc=" + pc +
					'}';
		}
	}
}
