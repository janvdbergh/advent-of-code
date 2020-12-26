package eu.janvdb.aoc2017.day18;

import eu.janvdb.aocutil.java.InputReader;

import java.util.Collections;
import java.util.List;

public class Puzzle {

	public static void main(String[] args) throws InterruptedException {
		List<Instruction> instructions = InputReader.readInput(Puzzle.class.getResource("input.txt"))
				.map(Puzzle::parseInstruction)
				.toJavaList();
		instructions = Collections.unmodifiableList(instructions);

		Communication communication = new Communication();
		Machine machine0 = new Machine(0, instructions, communication);
		Machine machine1 = new Machine(1, instructions, communication);

		Thread thread0 = startMachine(machine0);
		Thread thread1 = startMachine(machine1);

		thread0.join();
		thread1.join();
	}

	private static Thread startMachine(Machine machine) {
		Thread thread = new Thread(() -> runMachine(machine), "machine" + machine.getId());
		thread.start();
		return thread;
	}

	private static void runMachine(Machine machine) {
		try {
			machine.run();
		} catch (DeadLockedException e) {
			System.out.println("Deadlock for machine " + machine);
		}
	}

	private static Instruction parseInstruction(String instructionStr) {
		String[] parts = instructionStr.split("\\s+");

		return switch (parts[0]) {
			case "snd" -> new Snd(parts[1]);
			case "rcv" -> new Rcv(parts[1]);
			case "set" -> new Set(parts[1], parts[2]);
			case "add" -> new Add(parts[1], parts[2]);
			case "mul" -> new Mul(parts[1], parts[2]);
			case "mod" -> new Mod(parts[1], parts[2]);
			case "jgz" -> new Jgz(parts[1], parts[2]);
			default -> throw new IllegalArgumentException(instructionStr);
		};
	}

	private static class Snd implements Instruction {
		private final String register;

		Snd(String register) {
			this.register = register;
		}

		@Override
		public void execute(Machine machine) {
			machine.snd(register);
		}

		@Override
		public String toString() {
			return "snd " + register;
		}
	}

	private static class Rcv implements Instruction {
		private final String register;

		Rcv(String register) {
			this.register = register;
		}

		@Override
		public void execute(Machine machine) throws DeadLockedException {
			machine.rcv(register);
		}

		@Override
		public String toString() {
			return "rcv " + register;
		}
	}

	private static class Set implements Instruction {
		private final String register;
		private final String value;

		Set(String register, String value) {
			this.register = register;
			this.value = value;
		}

		@Override
		public void execute(Machine machine) {
			machine.set(register, value);
		}

		@Override
		public String toString() {
			return "set " + register + " " + value;
		}
	}

	private static class Add implements Instruction {
		private final String register;
		private final String value;

		Add(String register, String value) {
			this.register = register;
			this.value = value;
		}

		@Override
		public void execute(Machine machine) {
			machine.add(register, value);
		}

		@Override
		public String toString() {
			return "add " + register + " " + value;
		}
	}

	private static class Mul implements Instruction {
		private final String register;
		private final String value;

		Mul(String register, String value) {
			this.register = register;
			this.value = value;
		}

		@Override
		public void execute(Machine machine) {
			machine.mul(register, value);
		}

		@Override
		public String toString() {
			return "mul " + register + " " + value;
		}
	}

	private static class Mod implements Instruction {
		private final String register;
		private final String value;

		Mod(String register, String value) {
			this.register = register;
			this.value = value;
		}

		@Override
		public void execute(Machine machine) {
			machine.mod(register, value);
		}

		@Override
		public String toString() {
			return "mod " + register + " " + value;
		}
	}

	private static class Jgz implements Instruction {
		private final String conditionValue;
		private final String jumpValue;

		Jgz(String conditionValue, String jumpValue) {
			this.conditionValue = conditionValue;
			this.jumpValue = jumpValue;
		}

		@Override
		public void execute(Machine machine) {
			machine.jgz(conditionValue, jumpValue);
		}

		@Override
		public String toString() {
			return "jgz " + conditionValue + " " + jumpValue;
		}
	}
}
