package eu.janvdb.aoc2019.day23;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Network {

	private final Map<Integer, NetworkDevice> devices = new HashMap<>();
	private List<NetworkMonitor> monitors = new ArrayList<>();

	public void addDevice(NetworkDevice device) {
		device.registerNetworkSender(this::messageSent);
		devices.put(device.getAddress(), device);
	}

	public void addMonitor(NetworkMonitor monitor) {
		this.monitors.add(monitor);
	}

	private void messageSent(NetworkMessage message) {
		monitors.forEach(networkMonitor -> networkMonitor.monitorMessage(message));
		NetworkDevice recipient = devices.get(message.getRecipient());
		if (recipient != null) {
			recipient.receiveMessage(message);
		}
	}

	public void start() {
		this.devices.values().forEach(NetworkDevice::start);
	}
}
