package eu.janvdb.aoc2016.day11;

import io.vavr.collection.LinkedHashSet;
import io.vavr.collection.List;
import io.vavr.collection.Set;

import java.util.Comparator;

import static io.vavr.Function1.identity;

public class State {

	private static final int BOTTOM_FLOOR = 1;
	private static final int TOP_FLOOR = 4;

	private final int elevatorFloor;
	private final List<Floors> floorsList;

	private State(int elevatorFloor, List<Floors> floorsList) {
		this.elevatorFloor = elevatorFloor;

		this.floorsList = floorsList
				.sorted(Comparator.comparing(identity()));
	}

	public boolean isSolved() {
		return elevatorFloor == TOP_FLOOR &&
				floorsList.foldLeft(true, (previousResult, floors) -> previousResult && floors.isSolved());
	}

	public Set<State> getConnectedStates() {
		List<Integer> itemsOnElevatorFloor = getCodedListOfItemsOnElevatorFloor();

		Set<State> result = LinkedHashSet.empty();
		if (elevatorFloor > BOTTOM_FLOOR) {
			result = result.addAll(
					itemsOnElevatorFloor.toStream()
							.flatMap(item1 -> itemsOnElevatorFloor.toStream()
									.map(item2 -> moveItems(item1, item2, elevatorFloor - 1))
							)
							.filter(State::isValid)
			);
		}
		if (elevatorFloor < TOP_FLOOR) {
			result = result.addAll(
					itemsOnElevatorFloor.toStream()
							.flatMap(item1 -> itemsOnElevatorFloor.toStream()
									.map(item2 -> moveItems(item1, item2, elevatorFloor + 1))
							)
							.filter(State::isValid)
			);
		}

		return result;
	}

	private List<Integer> getCodedListOfItemsOnElevatorFloor() {
		List<Integer> itemsOnElevatorFloor = List.empty();
		for (int i = 0; i < floorsList.size(); i++) {
			Floors floors = floorsList.get(i);
			if (floors.getChipFloor() == elevatorFloor) itemsOnElevatorFloor = itemsOnElevatorFloor.append(i + 1);
			if (floors.getGeneratorFloor() == elevatorFloor) itemsOnElevatorFloor = itemsOnElevatorFloor.append(-i - 1);
		}
		return itemsOnElevatorFloor;
	}

	private boolean isValid() {
		return floorsList
				.find(this::isItemInvalid)
				.isEmpty();
	}

	private boolean isItemInvalid(Floors item) {
		return item.getChipFloor() != item.getGeneratorFloor() &&
				floorsList
						.find(otherItem -> item.chipFloor == otherItem.generatorFloor)
						.isDefined();
	}

	private State moveItems(Integer item1, Integer item2, int newElevatorFloor) {
		List<Floors> newFloorsForChipAndGenerator = floorsList;
		newFloorsForChipAndGenerator = moveOneItem(newFloorsForChipAndGenerator, item1, newElevatorFloor);
		newFloorsForChipAndGenerator = moveOneItem(newFloorsForChipAndGenerator, item2, newElevatorFloor);

		return new State(newElevatorFloor, newFloorsForChipAndGenerator);
	}

	private List<Floors> moveOneItem(List<Floors> floorsList, Integer codedItem, int newFloor) {
		if (codedItem < 0) {
			int index = -codedItem - 1;
			Floors tempFloors = floorsList.get(index);
			return floorsList
					.removeAt(index)
					.insert(index, new Floors(tempFloors.chipFloor, newFloor));
		} else {
			int index = codedItem - 1;
			Floors tempFloors = floorsList.get(index);
			return floorsList
					.removeAt(index)
					.insert(index, new Floors(newFloor, tempFloors.generatorFloor));
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		State state = (State) o;
		return elevatorFloor == state.elevatorFloor && floorsList.equals(state.floorsList);
	}

	@Override
	public int hashCode() {
		int result = elevatorFloor;
		result = 31 * result + floorsList.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "State{" +
				"elevatorFloor=" + elevatorFloor +
				", floorsList=" + floorsList +
				'}';
	}

	public static class Builder {

		private List<Floors> floorsForChipAndGenerator = List.empty();
		private int elevatorFloor = 1;

		static Builder aState() {
			return new Builder();
		}

		public Builder withItem(int floorOfChip, int floorOfGenerator) {
			floorsForChipAndGenerator = floorsForChipAndGenerator
					.append(new Floors(floorOfChip, floorOfGenerator));
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

	private static class Floors implements Comparable<Floors> {
		private final int chipFloor, generatorFloor;

		private Floors(int chipFloor, int generatorFloor) {
			this.chipFloor = chipFloor;
			this.generatorFloor = generatorFloor;
		}

		int getChipFloor() {
			return chipFloor;
		}

		int getGeneratorFloor() {
			return generatorFloor;
		}

		boolean isSolved() {
			return chipFloor == TOP_FLOOR && generatorFloor == TOP_FLOOR;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			Floors other = (Floors) o;
			return chipFloor == other.chipFloor && generatorFloor == other.generatorFloor;
		}

		@Override
		public int hashCode() {
			int result = chipFloor;
			result = 31 * result + generatorFloor;
			return result;
		}

		@Override
		public int compareTo(Floors o) {
			int compare1 = chipFloor - o.chipFloor;
			return compare1 != 0 ? compare1 : generatorFloor - o.generatorFloor;
		}

		@Override
		public String toString() {
			return "Floors{" +
					"chipFloor=" + chipFloor +
					", generatorFloor=" + generatorFloor +
					'}';
		}
	}
}


