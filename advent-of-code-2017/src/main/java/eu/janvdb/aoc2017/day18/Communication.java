package eu.janvdb.aoc2017.day18;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Communication {

	private final Map<Long, LinkedList<Long>> buffersPerMachine = new HashMap<>();
	private int numberWaiting = 0;

	public synchronized void register(long machineId) {
		buffersPerMachine.put(machineId, new LinkedList<>());
	}

	public synchronized void send(long machineId, long value) {
		buffersPerMachine.keySet().stream()
				.filter(id -> id != machineId)
				.forEach(id -> buffersPerMachine.get(id).addLast(value));

		notifyAll();
	}

	public synchronized long receive(long machineId) throws DeadLockedException {
		numberWaiting++;

		if (isDeadlocked()) {
			notifyAll();
			throw new DeadLockedException();
		}

		LinkedList<Long> queue = buffersPerMachine.get(machineId);
		while (queue.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.out.println("Interrupted!");
				throw new RuntimeException(e);
			}

			if (isDeadlocked()) {
				throw new DeadLockedException();
			}
		}

		numberWaiting--;
		return queue.removeFirst();
	}

	private boolean isDeadlocked() {
		return numberWaiting == buffersPerMachine.size() &&
				buffersPerMachine.values().stream().allMatch(queue -> queue.size()==0);
	}
}
