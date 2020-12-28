package eu.janvdb.aoc2018.day23;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Day23 {

	public static void main(String[] args) {
		Swarm swarm = Swarm.parse("day23.txt");
		part1(swarm);
		part2(swarm);
	}

	private static void part1(Swarm swarm) {
		Nanobot strongest = swarm.getStrongest();
		int nanobotsInRange = swarm.findNumberOfNanobotsInRangeOf(strongest);
		System.out.println(nanobotsInRange);
	}

	private static void part2(Swarm swarm) {
		PossibleSolution possibleSolution = new PossibleSolution(swarm.getBoundingCube(), swarm);
		PriorityQueue<PossibleSolution> toDo = new PriorityQueue<>(Comparator.comparing(PossibleSolution::getScore).reversed());
		toDo.add(possibleSolution);

		PossibleSolution bestSolution = null;
		while (!toDo.isEmpty()) {
			PossibleSolution current = toDo.poll();

			if (current.getCube().getRadius() == 0) {
				bestSolution = current;
				toDo.removeIf(solution -> solution.getScore() <= current.getScore());
			} else {
				List<PossibleSolution> split = current.split();
				toDo.addAll(split);
			}
		}

		System.out.println(bestSolution);
	}
}
