package eu.janvdb.aoc2016.day10;


import io.vavr.collection.List;

public class Bot extends Receiver {

	private Instruction instruction;
	private boolean hasMoved;
	private List<Integer> values = List.empty();

	Bot(int number) {
		super(number);
	}

	public void setInstruction(Instruction instruction) {
		if (this.instruction!=null) {
			throw new IllegalStateException();
		}
		this.instruction = instruction;
	}

	@Override
	public void receive(int value) {
		if (values.size()==2) {
			throw new IllegalStateException();
		}
		values = values.append(value);
	}

	public List<Integer> getValues() {
		return values;
	}

	public boolean execute() {
		if (hasMoved) {
			throw new IllegalStateException();
		}

		if (values.size()==2) {
			instruction.getReceiverLow().receive(Math.min(values.get(0), values.get(1)));
			instruction.getReceiverHigh().receive(Math.max(values.get(0), values.get(1)));
			hasMoved = true;
			return true;
		}

		return false;
	}

	@Override
	public String toString() {
		return "Bot " + getNumber() + ": " + values;
	}
}
