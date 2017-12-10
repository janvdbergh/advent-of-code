package eu.janvdb.aoc2016.day23;

import io.vavr.Function1;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.control.Option;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InstructionFactory {

	private static final List<Tuple2<Pattern, Function1<Matcher, Instruction>>> INSTRUCTIONS = List.of(
			Tuple.of(Pattern.compile("cpy ([abcd]|[+-]?\\d+) ([abcd])"), Cpy::new),
			Tuple.of(Pattern.compile("inc ([abcd])"), Inc::new),
			Tuple.of(Pattern.compile("dec ([abcd])"), Dec::new),
			Tuple.of(Pattern.compile("jnz ([abcd]|[+-]?\\d+) ([abcd]|[+-]?\\d+)"), Jnz::new),
			Tuple.of(Pattern.compile("tgl ([abcd]|[+-]?\\d+)"), Tgl::new),
			Tuple.of(Pattern.compile("addmul ([abcd]) ([abcd]) ([abcd])"), AddMul::new),
			Tuple.of(Pattern.compile("nop"), Nop::new)
	);

	public static Instruction assembleInstruction(String line) {
		for (Tuple2<Pattern, Function1<Matcher, Instruction>> instruction : INSTRUCTIONS) {
			Matcher matcher = instruction._1().matcher(line);
			if (matcher.matches()) {
				return instruction._2.apply(matcher);
			}
		}

		throw new IllegalArgumentException(line);
	}

	private static Option<Integer> getRegisterIndex(Matcher matcher, int argumentNumber) {
		String input = matcher.group(argumentNumber);
		if (input.matches("[a-d]")) {
			return Option.of(input.charAt(0) - 'a');
		}
		return Option.none();
	}

	private static long getRegisterOrValue(State state, Matcher matcher, int argumentNumber) {
		String input = matcher.group(argumentNumber);
		if (input.matches("[a-d]")) {
			int index = input.charAt(0) - 'a';
			return state.getRegister(index);
		}
		return Long.parseLong(matcher.group(argumentNumber));
	}

	private static class Cpy extends Instruction {
		Cpy(Matcher matcher) {
			super(matcher);
		}

		@Override
		public State execute(State state) {
			return getRegisterIndex(matcher, 2).map(
					toIndex -> state
							.withRegister(toIndex, getRegisterOrValue(state, matcher, 1))
							.withPcIncrement(1)
			).getOrElse(
					() -> state.withPcIncrement(1)
			);
		}

		@Override
		public Instruction toggle() {
			return new Jnz(matcher);
		}
	}

	private static class Inc extends Instruction {
		Inc(Matcher matcher) {
			super(matcher);
		}

		@Override
		public State execute(State state) {
			return getRegisterIndex(matcher, 1).map(
					index -> state
							.withRegister(index, state.getRegister(index) + 1)
							.withPcIncrement(1)
			).getOrElse(
					() -> state.withPcIncrement(1)
			);
		}

		@Override
		public Instruction toggle() {
			return new Dec(matcher);
		}
	}

	private static class Dec extends Instruction {
		Dec(Matcher matcher) {
			super(matcher);
		}

		@Override
		public State execute(State state) {
			return getRegisterIndex(matcher, 1).map(
					index -> state
							.withRegister(index, state.getRegister(index) - 1)
							.withPcIncrement(1)
			).getOrElse(
					() -> state.withPcIncrement(1)
			);
		}

		@Override
		public Instruction toggle() {
			return new Inc(matcher);
		}
	}

	private static class Jnz extends Instruction {
		Jnz(Matcher matcher) {
			super(matcher);
		}

		@Override
		public State execute(State state) {
			long value = getRegisterOrValue(state, matcher, 1);
			int offset = (int) getRegisterOrValue(state, matcher, 2);
			if (value != 0) {
				return state.withPcIncrement(offset);
			} else {
				return state.withPcIncrement(1);
			}
		}

		@Override
		public Instruction toggle() {
			return new Cpy(matcher);
		}
	}

	private static class Tgl extends Instruction {
		Tgl(Matcher matcher) {
			super(matcher);
		}

		@Override
		public State execute(State state) {
			int addressToUpdate = state.getPc() + (int) getRegisterOrValue(state, matcher, 1);
			return state.getInstruction(addressToUpdate)
					.map(Instruction::toggle)
					.map(instruction -> state.withInstruction(addressToUpdate, instruction))
					.getOrElse(state)
					.withPcIncrement(1);
		}

		@Override
		public Instruction toggle() {
			return new Inc(matcher);
		}
	}

	private static class AddMul extends Instruction {
		AddMul(Matcher matcher) {
			super(matcher);
		}

		@Override
		public State execute(State state) {
			int reg1 = getRegisterIndex(matcher, 1).getOrElseThrow(IllegalStateException::new);
			int reg2 = getRegisterIndex(matcher, 2).getOrElseThrow(IllegalStateException::new);
			int reg3 = getRegisterIndex(matcher, 3).getOrElseThrow(IllegalStateException::new);

			return state
					.withRegister(reg1, state.getRegister(reg1) + state.getRegister(reg2) * state.getRegister(reg3))
					.withPcIncrement(1);
		}

		@Override
		public Instruction toggle() {
			throw new IllegalStateException();
		}
	}

	private static class Nop extends Instruction {
		Nop(Matcher matcher) {
			super(matcher);
		}

		@Override
		public State execute(State state) {
			return state
					.withPcIncrement(1);
		}

		@Override
		public Instruction toggle() {
			throw new IllegalStateException();
		}
	}

}
