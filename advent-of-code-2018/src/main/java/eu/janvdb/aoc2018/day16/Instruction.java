package eu.janvdb.aoc2018.day16;

public class Instruction {

	private static final int ADDR = 1;
	private static final int ADDI = 3;
	private static final int MULR = 2;
	private static final int MULI = 13;
	private static final int BANR = 5;
	private static final int BANI = 0;
	private static final int BORR = 6;
	private static final int BORI = 10;
	private static final int SETR = 11;
	private static final int SETI = 8;
	private static final int GTIR = 15;
	private static final int GTRI = 4;
	private static final int GTRR = 14;
	private static final int EQIR = 12;
	private static final int EQRI = 7;
	private static final int EQRR = 9;

	private final int opcode;
	private final int arg0, arg1, arg2;

	public Instruction(String description) {
		// e.g. 2 0 2 1 or addr 0 2 1
		String[] parts = description.split("\\s+");
		this.opcode = parseOpcode(parts[0]);
		this.arg0 = Integer.parseInt(parts[1]);
		this.arg1 = Integer.parseInt(parts[2]);
		this.arg2 = Integer.parseInt(parts[3]);
	}

	private int parseOpcode(String part) {
		return switch (part) {
			case "addr" -> ADDR;
			case "addi" -> ADDI;
			case "mulr" -> MULR;
			case "muli" -> MULI;
			case "banr" -> BANR;
			case "bani" -> BANI;
			case "borr" -> BORR;
			case "bori" -> BORI;
			case "setr" -> SETR;
			case "seti" -> SETI;
			case "gtir" -> GTIR;
			case "gtri" -> GTRI;
			case "gtrr" -> GTRR;
			case "eqir" -> EQIR;
			case "eqri" -> EQRI;
			case "eqrr" -> EQRR;
			default -> Integer.parseInt(part);
		};
	}

	private Instruction(int opcode, int arg0, int arg1, int arg2) {
		this.opcode = opcode;
		this.arg0 = arg0;
		this.arg1 = arg1;
		this.arg2 = arg2;
	}

	public void execute(RegisterFile registerFile) {
		switch (opcode) {
			case ADDR -> addr(registerFile);
			case ADDI -> addi(registerFile);
			case MULR -> mulr(registerFile);
			case MULI -> muli(registerFile);
			case BANR -> banr(registerFile);
			case BANI -> bani(registerFile);
			case BORR -> borr(registerFile);
			case BORI -> bori(registerFile);
			case SETR -> setr(registerFile);
			case SETI -> seti(registerFile);
			case GTIR -> gtir(registerFile);
			case GTRI -> gtri(registerFile);
			case GTRR -> gtrr(registerFile);
			case EQIR -> eqir(registerFile);
			case EQRI -> eqri(registerFile);
			case EQRR -> eqrr(registerFile);
			default -> throw new IllegalArgumentException();
		}
	}

	private void addr(RegisterFile registerFile) {
		registerFile.set(arg2, registerFile.get(arg0) + registerFile.get(arg1));
	}

	private void addi(RegisterFile registerFile) {
		registerFile.set(arg2, registerFile.get(arg0) + arg1);
	}

	private void mulr(RegisterFile registerFile) {
		registerFile.set(arg2, registerFile.get(arg0) * registerFile.get(arg1));
	}

	private void muli(RegisterFile registerFile) {
		registerFile.set(arg2, registerFile.get(arg0) * arg1);
	}

	private void banr(RegisterFile registerFile) {
		registerFile.set(arg2, registerFile.get(arg0) & registerFile.get(arg1));
	}

	private void bani(RegisterFile registerFile) {
		registerFile.set(arg2, registerFile.get(arg0) & arg1);
	}

	private void borr(RegisterFile registerFile) {
		registerFile.set(arg2, registerFile.get(arg0) | registerFile.get(arg1));
	}

	private void bori(RegisterFile registerFile) {
		registerFile.set(arg2, registerFile.get(arg0) | arg1);
	}

	private void setr(RegisterFile registerFile) {
		registerFile.set(arg2, registerFile.get(arg0));
	}

	private void seti(RegisterFile registerFile) {
		registerFile.set(arg2, arg0);
	}

	private void gtir(RegisterFile registerFile) {
		// gtir (greater-than immediate/register) sets register C to 1 if value A is greater than register B. Otherwise, register C is set to 0.
		registerFile.set(arg2, arg0 > registerFile.get(arg1) ? 1 : 0);
	}

	private void gtri(RegisterFile registerFile) {
		// gtri (greater-than register/immediate) sets register C to 1 if register A is greater than value B. Otherwise, register C is set to 0.
		registerFile.set(arg2, registerFile.get(arg0) > arg1 ? 1 : 0);
	}

	private void gtrr(RegisterFile registerFile) {
		// gtrr (greater-than register/register) sets register C to 1 if register A is greater than register B. Otherwise, register C is set to 0.
		registerFile.set(arg2, registerFile.get(arg0) > registerFile.get(arg1) ? 1 : 0);
	}

	private void eqir(RegisterFile registerFile) {
		// eqir (equal immediate/register) sets register C to 1 if value A is equal to register B. Otherwise, register C is set to 0.
		registerFile.set(arg2, arg0 == registerFile.get(arg1) ? 1 : 0);
	}

	private void eqri(RegisterFile registerFile) {
		// eqri (equal register/immediate) sets register C to 1 if register A is equal to value B. Otherwise, register C is set to 0.
		registerFile.set(arg2, registerFile.get(arg0) == arg1 ? 1 : 0);
	}

	private void eqrr(RegisterFile registerFile) {
		// eqrr (equal register/register) sets register C to 1 if register A is equal to register B. Otherwise, register C is set to 0.
		registerFile.set(arg2, registerFile.get(arg0) == registerFile.get(arg1) ? 1 : 0);
	}

	Instruction withOpcode(int opcode) {
		return new Instruction(opcode, arg0, arg1, arg2);
	}
}
