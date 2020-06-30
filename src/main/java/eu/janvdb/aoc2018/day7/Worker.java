package eu.janvdb.aoc2018.day7;

import java.util.Optional;

class Worker {

	private final int id;

	private int currentTime;
	private Step currentStep;
	private int currentStepStartedAt;

	Worker(int id) {
		this.id = id;
		System.out.printf("Worker %d created.\n", id);
	}

	boolean isIdle() {
		return currentStep == null;
	}

	void startStep(Step step) {
		if (currentStep != null) throw new IllegalStateException();
		currentStep = step;
		currentStepStartedAt = currentTime;

		System.out.printf("Worker %d started step %s at %d.\n", id, currentStep, currentTime);

	}

	Optional<Step> tickAndReturnedFinishedStep() {
		if (currentStep != null && currentStepStartedAt + currentStep.getDuration() - 1 == currentTime) {
			System.out.printf("Worker %d finished step %s at %d.\n", id, currentStep, currentTime);

			Optional<Step> result = Optional.of(this.currentStep);
			currentStep = null;
			currentTime++;
			return result;
		}

		currentTime++;
		return Optional.empty();
	}
}
