package eu.janvdb.aoc2019.day24;

import java.util.function.Consumer;

public interface NetworkDevice {

	int getAddress();

	void registerNetworkSender(Consumer<NetworkMessage> messageConsumer);
	void receiveMessage(NetworkMessage networkMessage);

	void start();
}
