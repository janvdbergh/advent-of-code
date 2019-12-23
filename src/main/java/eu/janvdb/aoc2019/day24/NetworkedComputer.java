package eu.janvdb.aoc2019.day24;

import java.util.function.Consumer;

import eu.janvdb.aoc2019.common.Computer;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

public class NetworkedComputer implements NetworkDevice {

	private final int address;
	private final Computer computer;
	private final Subject<Long> computerInput;

	public NetworkedComputer(int address, long[] program) {
		this.address = address;
		this.computer = new Computer(program);
		this.computerInput = BehaviorSubject.create();

		this.computer.reconnectInput(computerInput, -1L);
		computerInput.onNext((long) address);
	}
	
	@Override
	public int getAddress() {
		return address;
	}

	@Override
	public boolean isIdle() {
		return computer.isIdle();
	}

	@Override
	public void registerNetworkSender(Consumer<NetworkMessage> messageConsumer) {
		computer.reconnectOutput().buffer(3)
				.map(values -> new NetworkMessage(address, values.get(0).intValue(), values.get(1), values.get(2)))
				.subscribe(messageConsumer::accept);
	}

	@Override
	public void receiveMessage(NetworkMessage networkMessage) {
		computerInput.onNext(networkMessage.getX());
		computerInput.onNext(networkMessage.getY());
	}

	@Override
	public void start() {
		new Thread(computer::run, "Computer " + address).start();
	}
}
