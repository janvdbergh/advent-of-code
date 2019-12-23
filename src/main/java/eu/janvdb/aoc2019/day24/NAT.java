package eu.janvdb.aoc2019.day24;

import java.util.function.Consumer;

public class NAT implements NetworkDevice {

	private static final int NAT_ADDRESS = 255;
	private final Network network;

	private Consumer<NetworkMessage> messageConsumer;
	private NetworkMessage lastMessage;

	public NAT(Network network) {
		this.network = network;
	}

	@Override
	public int getAddress() {
		return NAT_ADDRESS;
	}

	@Override
	public boolean isIdle() {
		return true;
	}

	@Override
	public void registerNetworkSender(Consumer<NetworkMessage> messageConsumer) {
		this.messageConsumer = messageConsumer;
	}

	@Override
	public void receiveMessage(NetworkMessage networkMessage) {
		if (networkMessage.getRecipient() == 255) {
			this.lastMessage = networkMessage;
		}
	}

	@Override
	public void start() {
		new Thread(this::run, "NAT").start();
	}

	private void run() {
		try {
			while (true) {
				Thread.sleep(125);

				boolean allIdle = network.getDevices().stream().allMatch(NetworkDevice::isIdle);
				if (allIdle && lastMessage != null) {
					messageConsumer.accept(new NetworkMessage(NAT_ADDRESS, 0, lastMessage.getX(), lastMessage.getY()));
				}
			}
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
