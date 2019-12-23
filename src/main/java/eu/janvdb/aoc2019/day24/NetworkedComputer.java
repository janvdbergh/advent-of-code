package eu.janvdb.aoc2019.day24;

import eu.janvdb.aoc2019.common.ThreadedComputer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

public class NetworkedComputer extends ThreadedComputer implements NetworkDevice {

	private final BlockingQueue<Long> inputQueue = new LinkedBlockingQueue<>();
	private final List<Long> outputs = new ArrayList<>();
	private final int address;
	private Consumer<NetworkMessage> messageConsumer;

	public NetworkedComputer(int address, long[] program) {
		super(program);
		this.address = address;
		this.inputQueue.add((long) address);
	}

	@Override
	protected Long produceInput() {
		try {
			if (inputQueue.isEmpty()) {
				return -1L;
			}
			return inputQueue.take();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected synchronized void handleOutput(Long value) {
		outputs.add(value);
		if (outputs.size() == 3) {
			NetworkMessage message = new NetworkMessage(address, outputs.get(0).intValue(), outputs.get(1), outputs.get(2));
			outputs.clear();
			messageConsumer.accept(message);
		}
	}

	@Override
	public int getAddress() {
		return address;
	}

	@Override
	public void registerNetworkSender(Consumer<NetworkMessage> messageConsumer) {
		this.messageConsumer = messageConsumer;
	}

	@Override
	public void receiveMessage(NetworkMessage networkMessage) {
		inputQueue.add(networkMessage.getX());
		inputQueue.add(networkMessage.getY());
	}
}
