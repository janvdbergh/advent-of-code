package eu.janvdb.aoc2018.day24;

import java.util.Comparator;

public class Attack {

	public final static Comparator<Attack> ATTACKER_INITIATIVE_COMPARATOR = Comparator.comparing((Attack attack) -> attack.getAttacker().getInitiative()).reversed();
	public final static Comparator<Attack> TARGET_SELECTION_COMPARATOR = Comparator.comparing(Attack::getEffectiveDamage).reversed()
			.thenComparing(Comparator.comparing((Attack attack) -> attack.getDefender().getEffectivePower()).reversed())
			.thenComparing(Comparator.comparing((Attack attack) -> attack.getDefender().getInitiative()).reversed());

	private final Group attacker;
	private final Group defender;
	private final int effectiveDamage;

	public Attack(Group attacker, Group defender) {
		this.attacker = attacker;
		this.defender = defender;
		this.effectiveDamage = attacker.getEffectiveDamageWhenAttacking(defender);
	}

	public Group getAttacker() {
		return attacker;
	}

	public Group getDefender() {
		return defender;
	}

	public int getEffectiveDamage() {
		return effectiveDamage;
	}

	public boolean execute() {
		int newEffectiveDamage = attacker.getEffectiveDamageWhenAttacking(defender);
		return defender.undergoAttack(newEffectiveDamage);
	}
}
