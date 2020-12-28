package eu.janvdb.aoc2018.day24;

import java.util.List;
import java.util.stream.Collectors;

public class AttackSystem {

	private final String name;
	private final List<Group> groups;

	public AttackSystem(String name, List<Group> groups) {
		this.name = name;
		this.groups = groups;
	}

	public String getName() {
		return name;
	}

	public List<Group> getGroups() {
		return groups.stream().filter(group -> group.getUnitsRemaining() > 0).collect(Collectors.toList());
	}

	@Override
	public String toString() {
		return String.format("%s:%n%s",
				name,
				groups.stream().map(Group::toString).collect(Collectors.joining("\n"))
		);
	}

	public int getRemainingUnits() {
		return groups.stream().mapToInt(Group::getUnitsRemaining).sum();
	}

	public void setBoost(int boost) {
		groups.forEach(group -> group.setBoost(boost));
	}

	public void reset() {
		groups.forEach(Group::reset);
	}
}
