package eu.janvdb.aoc2016.day10;

class Instruction {

	private final Receiver receiverLow;
	private final Receiver receiverHigh;

	public Instruction(Receiver receiverLow, Receiver receiverHigh) {
		this.receiverLow = receiverLow;
		this.receiverHigh = receiverHigh;
	}

	public Receiver getReceiverLow() {
		return receiverLow;
	}

	public Receiver getReceiverHigh() {
		return receiverHigh;
	}
}
