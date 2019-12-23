package eu.janvdb.aoc2019.day24;

import java.util.ArrayList;
import java.util.Collection;
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

	private synchronized void messageSent(NetworkMessage message) {
		monitors.forEach(networkMonitor -> networkMonitor.receiveMessage(message));
		NetworkDevice recipient = devices.get(message.getRecipient());
		if (recipient != null) {
			recipient.receiveMessage(message);
		}
	}

	public void start() {
		this.devices.values().forEach(NetworkDevice::start);
	}

	public Collection<NetworkDevice> getDevices() {
		return devices.values();
	}
}
