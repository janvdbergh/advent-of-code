package eu.janvdb.aoc2017.day24;

import eu.janvdb.aocutil.java.InputReader;
import io.vavr.collection.List;
import io.vavr.collection.Stream;

import java.util.Comparator;

public class Puzzle {

	public static void main(String[] args) {
		List<Component> components = InputReader.readInput(Puzzle.class.getResource("input.txt"))
				.map(Component::new)
				.toList();

		part1(components);
		part2(components);
	}

	private static void part1(List<Component> components) {
		Bridge strongestBridge = getBridges(0, Bridge.empty(), components)
				.maxBy(Comparator.comparing(Bridge::getStrength))
				.getOrElseThrow(IllegalStateException::new);
		System.out.println(strongestBridge);
	}

	private static void part2(List<Component> components) {
		Bridge bestBridge = getBridges(0, Bridge.empty(), components)
				.maxBy(Comparator.comparing(Bridge::getLength).thenComparing(Bridge::getStrength))
				.getOrElseThrow(IllegalStateException::new);
		System.out.println(bestBridge);
	}

	private static Stream<Bridge> getBridges(int fromPort, Bridge currentBridge, List<Component> remainingComponents) {
		return remainingComponents.toStream()
				.filter(component -> component.matches(fromPort))
				.flatMap(component -> getBridges(component.getRemainingPort(fromPort), currentBridge.append(component), remainingComponents.remove(component)))
				.append(currentBridge);
	}
}
