package eu.janvdb.aoc2018.day7;

import eu.janvdb.aoc2018.util.FileReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day7 {

	private static final String FILE = "day7.txt";
	static final int NUMBER_OF_WORKERS = 5;
	static final int STEP_DURATION = 60;

	private static Pattern PATTERN = Pattern.compile("Step ([A-Z]) must be finished before step ([A-Z]) can begin.");

	public static void main(String[] args) throws IOException {
		// Step2 < 1188
		readStepOrderings();

		List<Step> stepOrdering = getStepOrdering();
		printStepOrdering(stepOrdering);

		WorkerPool workerPool = new WorkerPool();
		LinkedList<Step> toDo = new LinkedList<>(stepOrdering);
		List<Step> finished = new ArrayList<>();
		while (!toDo.isEmpty()) {
			Optional<Step> availableStep = getAvailableStep(toDo, finished);
			while(availableStep.isEmpty()) {
				workerPool.tick(finished::add);
				availableStep = getAvailableStep(toDo, finished);
			}

			while(!workerPool.hasIdleWorker()) {
				workerPool.tick(finished::add);
			}

			Step step = availableStep.get();
			toDo.remove(step);
			workerPool.startStep(step);
		}

		while (!workerPool.allWorkersIdle()) workerPool.tick(finished::add);
	}

	private static Optional<Step> getAvailableStep(LinkedList<Step> toDo, List<Step> finished) {
		return toDo.stream()
				.filter(step -> finished.containsAll(step.getPreviousSteps()))
				.findAny();
	}

	private static void readStepOrderings() throws IOException {
		FileReader.readStringFile(Day7.class, FILE)
				.forEach(Day7::processStepOrdering);
	}

	private static void processStepOrdering(String orderingDescription) {
		Matcher matcher = PATTERN.matcher(orderingDescription);
		if (!matcher.matches()) throw new IllegalArgumentException(orderingDescription);
		Step step1 = Step.getStep(matcher.group(1).charAt(0));
		Step step2 = Step.getStep(matcher.group(2).charAt(0));
		step2.addPreviousStep(step1);
	}

	private static void printStepOrdering(List<Step> stepOrdering) {
		stepOrdering.forEach(step -> System.out.print(step.getIdentifier()));
		System.out.println();
	}

	private static List<Step> getStepOrdering() {
		List<Step> placedSteps = new ArrayList<>();
		SortedSet<Step> remainingSteps = new TreeSet<>(Comparator.comparing(Step::getIdentifier));
		remainingSteps.addAll(Step.ALL_STEPS.values());

		while (!remainingSteps.isEmpty()) {
			Step nextStep = remainingSteps.stream()
					.filter(step -> placedSteps.containsAll(step.getPreviousSteps()))
					.findFirst().orElseThrow(IllegalStateException::new);
			placedSteps.add(nextStep);
			remainingSteps.remove(nextStep);
		}

		return placedSteps;
	}

}

