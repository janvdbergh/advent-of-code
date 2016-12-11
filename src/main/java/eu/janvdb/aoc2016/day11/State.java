package eu.janvdb.aoc2016.day11;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import eu.janvdb.util.ComparablePair;

public class State {

	private static final int TOP_FLOOR = 4;

	private final int elevatorFloor;
	private final List<ComparablePair<Integer, Integer>> floorsForChipAndGenerator;

	public State(int elevatorFloor, List<ComparablePair<Integer, Integer>> floorsForChipAndGenerator) {
		this.elevatorFloor = elevatorFloor;

		floorsForChipAndGenerator.sort(Comparator.comparing(Function.identity()));
		this.floorsForChipAndGenerator = Collections.unmodifiableList(floorsForChipAndGenerator);
	}

	public boolean isSolved() {
		return elevatorFloor == TOP_FLOOR &&
				floorsForChipAndGenerator.stream()
						.allMatch(pair -> pair.getV1() == 4 && pair.getV2() == TOP_FLOOR);
	}

	public Set<State> getConnectedStates() {
		List<Integer> itemsOnElevatorFloor = new ArrayList<>();
		for (int i = 0; i < floorsForChipAndGenerator.size(); i++) {
			ComparablePair<Integer, Integer> pair = floorsForChipAndGenerator.get(i);
			if (pair.getV1() == elevatorFloor) itemsOnElevatorFloor.add(i+1);
			if (pair.getV2() == elevatorFloor) itemsOnElevatorFloor.add(-i-1);
		}

		Set<State> result = new LinkedHashSet<>();
		if (elevatorFloor > 1) {
			itemsOnElevatorFloor.stream()
					.flatMap(item1 -> itemsOnElevatorFloor.stream().map(item2 -> moveItems(item1, item2, elevatorFloor - 1)))
					.filter(State::isValid)
					.forEach(result::add);
		}
		if (elevatorFloor < TOP_FLOOR) {
			itemsOnElevatorFloor.stream()
					.flatMap(item1 -> itemsOnElevatorFloor.stream().map(item2 -> moveItems(item1, item2, elevatorFloor + 1)))
					.filter(State::isValid)
					.forEach(result::add);
		}

		return result;
	}

	private boolean isValid() {
		return floorsForChipAndGenerator.stream()
				.allMatch(this::isItemValid);
	}

	private boolean isItemValid(ComparablePair<Integer, Integer> item) {
		return item.getV1().equals(item.getV2()) ||
				floorsForChipAndGenerator.stream().noneMatch(otherItem -> item.getV1().equals(otherItem.getV2()));
	}

	private State moveItems(Integer item1, Integer item2, int newElevatorFloor) {
		List<ComparablePair<Integer, Integer>> newFloorsForChipAndGenerator = new ArrayList<>(floorsForChipAndGenerator);
		moveOneItem(newFloorsForChipAndGenerator, item1, newElevatorFloor);
		moveOneItem(newFloorsForChipAndGenerator, item2, newElevatorFloor);

		return new State(newElevatorFloor, newFloorsForChipAndGenerator);
	}

	private void moveOneItem(List<ComparablePair<Integer, Integer>> floorsForChipAndGenerator, Integer item, int newFloor) {
		if (item < 0) {
			ComparablePair<Integer, Integer> oldPair = floorsForChipAndGenerator.remove(-item-1);
			floorsForChipAndGenerator.add(-item-1, new ComparablePair<>(oldPair.getV1(), newFloor));
		} else {
			ComparablePair<Integer, Integer> oldPair = floorsForChipAndGenerator.remove(item-1);
			floorsForChipAndGenerator.add(item-1, new ComparablePair<>(newFloor, oldPair.getV2()));
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		State state = (State) o;
		return elevatorFloor == state.elevatorFloor && floorsForChipAndGenerator.equals(state.floorsForChipAndGenerator);
	}

	@Override
	public int hashCode() {
		int result = elevatorFloor;
		result = 31 * result + floorsForChipAndGenerator.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "State{" +
				"elevatorFloor=" + elevatorFloor +
				", floorsForChipAndGenerator=" + floorsForChipAndGenerator +
				'}';
	}

	public static class Builder {

		private List<ComparablePair<Integer, Integer>> floorsForChipAndGenerator = new ArrayList<>();
		private int elevatorFloor = 1;

		public static Builder aState() {
			return new Builder();
		}

		public Builder withItem(int floorOfChip, int floorOfGenerator) {
			floorsForChipAndGenerator.add(new ComparablePair<>(floorOfChip, floorOfGenerator));
			return this;
		}

		public Builder withElevatorFloor(int elevatorFloor) {
			this.elevatorFloor = elevatorFloor;
			return this;
		}

		public State build() {
			return new State(elevatorFloor, floorsForChipAndGenerator);
		}
	}
}


