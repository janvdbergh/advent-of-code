package eu.janvdb.aoc2018.day24;

import java.util.Set;

public class Group {

	private final String id;
	private final int numberOfUnits;
	private final int hitPoints;
	private final Set<String> immunities;
	private final Set<String> weaknesses;
	private final String attackType;
	private final int attackDamage;
	private final int initiative;

	private int unitsRemaining;
	private int boost;

	Group(String id, int numberOfUnits, int hitPoints, Set<String> immunities, Set<String> weaknesses, String attackType, int attackDamage, int initiative) {
		this.id = id;
		this.numberOfUnits = numberOfUnits;
		this.hitPoints = hitPoints;
		this.immunities = immunities;
		this.weaknesses = weaknesses;
		this.attackType = attackType;
		this.attackDamage = attackDamage;
		this.initiative = initiative;

		this.unitsRemaining = numberOfUnits;
	}

	public void setBoost(int boost) {
		this.boost = boost;
	}

	public void reset() {
		this.unitsRemaining = this.numberOfUnits;
	}

	public int getEffectivePower() {
		return unitsRemaining * (attackDamage + boost);
	}

	public int getInitiative() {
		return initiative;
	}

	public int getUnitsRemaining() {
		return unitsRemaining;
	}

	public int getEffectiveDamageWhenAttacking(Group attackedGroup) {
		if (attackedGroup.immunities.contains(attackType)) return 0;
		int value = (attackDamage + boost) * unitsRemaining;
		return attackedGroup.weaknesses.contains(attackType) ? value * 2 : value;
	}

	@Override
	public String toString() {
		return String.format("%s contains %d units.", id, unitsRemaining);
	}

	public boolean undergoAttack(int effectiveDamage) {
		int unitsLost = Math.min(unitsRemaining, effectiveDamage / hitPoints);
		this.unitsRemaining -= unitsLost;
		return unitsLost != 0;
	}
}
