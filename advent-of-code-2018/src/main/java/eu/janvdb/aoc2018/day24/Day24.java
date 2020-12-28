package eu.janvdb.aoc2018.day24;

import eu.janvdb.aocutil.java.FileReader;

import java.util.Map;
import java.util.stream.Collectors;

public class Day24 {

	public static void main(String[] args) {
		new Day24().run();
	}

	private void run() {
		Map<String, AttackSystem> systems = FileReader.groupLines(FileReader.readStringFile(Day24.class, "day24.txt"))
				.stream().map(Parser::parseSystem)
				.collect(Collectors.toMap(AttackSystem::getName, system -> system));
		AttackSystem immuneSystem = systems.get("Immune System");
		AttackSystem infection = systems.get("Infection");

		part1(immuneSystem, infection);
		part2(immuneSystem, infection);
	}

	private void part1(AttackSystem immuneSystem, AttackSystem infection) {
		runBattleWithBoosts(immuneSystem, infection, 0);
		System.out.printf("%s / %s%n", immuneSystem.getRemainingUnits(), infection.getRemainingUnits());
	}

	private void part2(AttackSystem immuneSystem, AttackSystem infection) {
		int lowerBound = 0;
		int upperBound = 1;
		while (runBattleWithBoosts(immuneSystem, infection, upperBound) == 0) {
			lowerBound = upperBound;
			upperBound *= 2;
		}

		do {
			int middle = (lowerBound + upperBound) / 2;
			int remainingImmuneSystem = runBattleWithBoosts(immuneSystem, infection, middle);
			if (remainingImmuneSystem != 0) {
				upperBound = middle;
			} else {
				lowerBound = middle;
			}
		} while (upperBound - lowerBound > 1);

		System.out.printf("%d => %d%n", upperBound, runBattleWithBoosts(immuneSystem, infection, upperBound));
	}

	private int runBattleWithBoosts(AttackSystem immuneSystem, AttackSystem infection, int immuneSystemBoost) {
		immuneSystem.setBoost(immuneSystemBoost);

		boolean finished = runBattle(immuneSystem, infection);
		return finished ? immuneSystem.getRemainingUnits() : 0;
	}

	private boolean runBattle(AttackSystem immuneSystem, AttackSystem infection) {
		immuneSystem.reset();
		infection.reset();

		Battle battle = new Battle(immuneSystem, infection);
		boolean changed = true;
		while (changed && !battle.isFinished()) {
			changed = battle.runOneRound();
		}
		return battle.isFinished();
	}


}
