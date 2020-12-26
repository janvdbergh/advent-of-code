package eu.janvdb.aoc2018.day7;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

class Step {

	final static Map<Character, Step> ALL_STEPS = new HashMap<>();

	private final char identifier;
	private final Set<Step> previousSteps = new HashSet<>();

	static Step getStep(char identifier) {
		if (!ALL_STEPS.containsKey(identifier)) {
			ALL_STEPS.put(identifier, new Step(identifier));
		}
		return ALL_STEPS.get(identifier);
	}

	private Step(char identifier) {
		this.identifier = identifier;
	}

	char getIdentifier() {
		return identifier;
	}

	void addPreviousStep(Step step) {
		previousSteps.add(step);
	}

	Set<Step> getPreviousSteps() {
		return previousSteps;
	}

	int getDuration() {
		return Day7.STEP_DURATION + identifier - 'A' + 1;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Step)) return false;
		Step step = (Step) o;
		return identifier == step.identifier;
	}

	@Override
	public int hashCode() {
		return Objects.hash(identifier);
	}

	@Override
	public String toString() {
		return String.valueOf(identifier);
	}
}
