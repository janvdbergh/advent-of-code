package eu.janvdb.aoc2017.day18;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Machine {

	private final long id;
	private final List<Instruction> instructions;
	private final Communication communication;

	private int programCounter;
	private Map<String, Long> registers;
	private int numberOfValuesReceived;

	Machine(long id, List<Instruction> instructions, Communication communication) {
		this.id = id;
		this.instructions = instructions;
		this.communication = communication;
		this.communication.register(this.getId());
	}

	void run() throws DeadLockedException {
		resetState();
		while (programCounter >= 0 && programCounter < instructions.size()) {
			instructions.get(programCounter).execute(this);
		}
	}

	private void resetState() {
		this.programCounter = 0;
		this.registers = new HashMap<>();
		this.registers.put("p", id);
		this.numberOfValuesReceived = 0;
	}

	void snd(String expr) {
		communication.send(this.getId(), getValue(expr));
		programCounter += (int) (long) 1;
	}

	void rcv(String expr) throws DeadLockedException {
		setRegister(expr, communication.receive(this.getId()));
		numberOfValuesReceived++;

		programCounter += (int) (long) 1;
	}

	void set(String expr1, String expr2) {
		setRegister(expr1, getValue(expr2));
		programCounter += (int) (long) 1;
	}

	void add(String expr1, String expr2) {
		setRegister(expr1, getRegister(expr1) + getValue(expr2));
		programCounter += (int) (long) 1;
	}

	void mul(String expr1, String expr2) {
		setRegister(expr1, getRegister(expr1) * getValue(expr2));
		programCounter += (int) (long) 1;
	}

	void mod(String expr1, String expr2) {
		setRegister(expr1, getRegister(expr1) % getValue(expr2));
		programCounter += (int) (long) 1;
	}

	void jgz(String expr1, String expr2) {
		long value = getValue(expr1);
		if (value > 0) {
			programCounter += (int) getValue(expr2);
		} else {
			programCounter += 1;
		}
	}

	long getId() {
		return id;
	}

	private long getRegister(String name) {
		if (!registers.containsKey(name)) {
			registers.put(name, 0L);
		}

		return registers.get(name);
	}

	private void setRegister(String name, long value) {
		registers.put(name, value);
	}

	private long getValue(String nameOrValue) {
		if (nameOrValue.matches("[a-z]")) {
			return getRegister(nameOrValue);
		}
		return Long.parseLong(nameOrValue);
	}

	@Override
	public String toString() {
		return "Machine{" +
				"id=" + id +
				", programCounter=" + programCounter +
				", registers=" + registers +
				", numberOfValuesReceived=" + numberOfValuesReceived +
				'}';
	}
}
