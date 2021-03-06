package eu.janvdb.aoc2019.day23;

public class NetworkBuilder {

	public static Network build(int numberOfComputers, long[] program) {
		Network network = new Network();
		for (int address = 0; address < numberOfComputers; address++) {
			network.addDevice(new NetworkedComputer(address, program));
		}
		network.addDevice(new NAT());

		return network;
	}
}
