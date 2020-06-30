package eu.janvdb.aoc2018.day7;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

class WorkerPool {

	private final List<Worker> workers;

	WorkerPool() {
		workers = new ArrayList<>();
		for (int i = 0; i < Day7.NUMBER_OF_WORKERS; i++) workers.add(new Worker(i));
	}

	boolean hasIdleWorker() {
		return workers.stream().anyMatch(Worker::isIdle);
	}

	boolean allWorkersIdle() {
		return workers.stream().allMatch(Worker::isIdle);
	}

	void startStep(Step step) {
		Worker worker = workers.stream()
				.filter(Worker::isIdle)
				.findAny()
				.orElseThrow(IllegalStateException::new);
		worker.startStep(step);
	}

	void tick(Consumer<Step> finishedStepConsumer) {
		workers.forEach(worker -> worker.tickAndReturnedFinishedStep().ifPresent(finishedStepConsumer));
	}
}
