package eu.janvdb.aoc2016.day23;

import javaslang.collection.Array;
import javaslang.control.Option;

class State {

	private final Array<Instruction> program;
	private final Array<Long> registers;
	private final int pc;

	public State(Array<Instruction> program) {
		this.program = program;
		registers = Array.of(0L, 0L, 0L, 0L);
		pc = 0;
	}

	private State(Array<Instruction> program, Array<Long> registers, int pc) {
		this.program = program;
		this.registers = registers;
		this.pc = pc;
	}

	public State withInstruction(int address, Instruction newInstruction) {
		return new State(program.update(address, newInstruction), registers, pc);
	}

	public State withRegister(int register, long value) {
		return new State(program, registers.update(register, value), pc);
	}

	public State withPcIncrement(int pcIncrement) {
		return new State(program, registers, pc + pcIncrement);
	}

	public long getRegister(int index) {
		return registers.get(index);
	}

	public int getPc() {
		return pc;
	}

	public Option<Instruction> getInstruction(int address) {
		return address<program.length() ? Option.of(program.get(address)) : Option.none();
	}

	public boolean hasInstruction() {
		return pc < program.size();
	}

	public Instruction getCurrentInstruction() {
		return program.get(pc);
	}

	@Override
	public String toString() {
		return "State{" +
				"pc=" + pc +
				", registers=" + registers +
				'}';
	}
}
