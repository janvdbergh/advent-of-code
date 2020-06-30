package eu.janvdb.aoc2018.day19;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import eu.janvdb.aoc2018.day16.Instruction;
import eu.janvdb.aoc2018.day16.RegisterFile;

public class Machine {

	private static final Pattern IP_PATTERN = Pattern.compile("#ip (\\d)+");

	private final List<Instruction> instructions;
	private int ipRegister;
	private Set<Long> breakpoints = new HashSet<>();

	public Machine(List<String> instructions) {
		this.instructions = instructions.stream()
				.filter(instruction -> !instruction.startsWith("#"))
				.map(Instruction::new)
				.collect(Collectors.toList());

		instructions.stream()
				.filter(instruction -> instruction.startsWith("#"))
				.forEach(this::handleMetaInstruction);
	}

	private void handleMetaInstruction(String instruction) {
		Matcher ipMatcher = IP_PATTERN.matcher(instruction);
		if (ipMatcher.matches()) {
			ipRegister = Integer.parseInt(ipMatcher.group(1));
		} else {
			throw new IllegalArgumentException(instruction);
		}
	}

	public void run(RegisterFile registers) {
		while (!breakpoints.contains(getInstructionPointer(registers)) && getInstructionPointer(registers) >= 0 && getInstructionPointer(registers) < instructions.size()) {
			step(registers);
		}
	}

	private long getInstructionPointer(RegisterFile registers) {
		return registers.get(ipRegister);
	}

	public void step(RegisterFile registers) {
		Instruction instruction = instructions.get((int) getInstructionPointer(registers));
		instruction.execute(registers);

		registers.set(ipRegister, getInstructionPointer(registers) + 1);
	}

	public void addBreakpoint(int address) {
		breakpoints.add((long) address);
	}
}
