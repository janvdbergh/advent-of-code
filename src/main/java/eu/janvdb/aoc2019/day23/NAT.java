package eu.janvdb.aoc2019.day23;

import java.util.function.Consumer;

public class NAT implements NetworkDevice, NetworkMonitor {

	private static final int NAT_ADDRESS = 255;

	private Consumer<NetworkMessage> messageConsumer;
	private NetworkMessage lastMessage;
	private long lastMessageTimeStamp = System.currentTimeMillis() + 2000L;

	@Override
	public int getAddress() {
		return NAT_ADDRESS;
	}

	@Override
	public void registerNetworkSender(Consumer<NetworkMessage> messageConsumer) {
		this.messageConsumer = messageConsumer;
	}

	@Override
	public void receiveMessage(NetworkMessage networkMessage) {
		this.lastMessage = networkMessage;
	}

	@Override
	public void monitorMessage(NetworkMessage message) {
		this.lastMessageTimeStamp = System.currentTimeMillis();
	}

	@Override
	public void start() {
		new Thread(this::run, "NAT").start();
	}

	private void run() {
		try {
			while (true) {
				Thread.sleep(250);
				if (System.currentTimeMillis() - lastMessageTimeStamp > 250) {
					messageConsumer.accept(new NetworkMessage(NAT_ADDRESS, 0, lastMessage.getX(), lastMessage.getY()));
				}
			}
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
