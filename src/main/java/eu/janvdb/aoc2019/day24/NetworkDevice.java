package eu.janvdb.aoc2019.day24;

import java.util.function.Consumer;

public interface NetworkDevice {

	int getAddress();
	boolean isIdle();

	void registerNetworkSender(Consumer<NetworkMessage> messageConsumer);
	void receiveMessage(NetworkMessage networkMessage);

	void start();
}
