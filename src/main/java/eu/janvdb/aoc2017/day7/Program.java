package eu.janvdb.aoc2017.day7;

import javaslang.collection.Map;
import javaslang.collection.Set;

import java.util.Objects;

public class Program {

	private final String name;
	private final int weight;
	private final Set<String> childNames;
	private Set<Program> children;
	private Program parent;

	Program(String name, int weight, Set<String> childNames) {
		this.name = name;
		this.weight = weight;
		this.childNames = childNames;
	}

	public String getName() {
		return name;
	}

	public int getWeight() {
		return weight;
	}

	public int getTotalWeight() {
		return weight + children.toStream()
				.map(Program::getTotalWeight)
				.sum().intValue();
	}

	public boolean isBalanced() {
		Set<Integer> weights = getChildren().map(Program::getTotalWeight);
		return weights.size() <= 1;
	}

	public Set<Program> getChildren() {
		return children;
	}

	public Program getParent() {
		return parent;
	}

	public void mapPrograms(Map<String, Program> allPrograms) {
		children = childNames.map(name -> allPrograms.get(name).getOrElseThrow(IllegalArgumentException::new));
		children.forEach(child -> child.setParent(this));
	}

	private void setParent(Program parent) {
		this.parent = parent;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Program program = (Program) o;
		return Objects.equals(name, program.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
}
