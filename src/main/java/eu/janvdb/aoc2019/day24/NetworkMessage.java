package eu.janvdb.aoc2019.day24;

public class NetworkMessage {

	private final int sender;
	private final int recipient;
	private final long x, y;

	public NetworkMessage(int sender, int recipient, long x, long y) {
		this.sender = sender;
		this.recipient = recipient;
		this.x = x;
		this.y = y;
	}

	public int getSender() {
		return sender;
	}

	public int getRecipient() {
		return recipient;
	}

	public long getX() {
		return x;
	}

	public long getY() {
		return y;
	}

	public String toString() {
		return String.format("<%d -> %d> (%d, %d)", sender, recipient, x, y);
	}
}
