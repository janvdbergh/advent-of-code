package eu.janvdb.aoc2018.day24;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class Battle {

	private static final Comparator<Group> GROUP_COMPARATOR_INITIATIVE = Comparator.comparing(Group::getInitiative).reversed();
	private static final Comparator<Group> GROUP_COMPARATOR_EFFECTIVE_POWER_THEN_INITIATIVE =
			Comparator.comparing(Group::getEffectivePower).reversed().thenComparing(GROUP_COMPARATOR_INITIATIVE);

	private final AttackSystem immuneSystem;
	private final AttackSystem infection;

	public Battle(AttackSystem immuneSystem, AttackSystem infection) {
		this.immuneSystem = immuneSystem;
		this.infection = infection;
	}

	public boolean runOneRound() {
		SortedSet<Attack> attacks = new TreeSet<>(Attack.ATTACKER_INITIATIVE_COMPARATOR);
		chooseTargets(infection, immuneSystem, attacks);
		chooseTargets(immuneSystem, infection, attacks);

		boolean ok = false;
		for (Attack attack : attacks) {
			if (attack.execute()) ok = true;
		}
		return ok;
	}

	private void chooseTargets(AttackSystem attackers, AttackSystem defenders, SortedSet<Attack> attacks) {
		List<Group> attackersInOrder = attackers.getGroups();
		attackersInOrder.sort(GROUP_COMPARATOR_EFFECTIVE_POWER_THEN_INITIATIVE);

		Set<Group> remainingDefenders = new HashSet<>(defenders.getGroups());
		for (Group attacker : attackersInOrder) {
			remainingDefenders.stream()
					.map(defender -> new Attack(attacker, defender))
					.filter(attack -> attack.getEffectiveDamage() != 0)
					.min(Attack.TARGET_SELECTION_COMPARATOR)
					.ifPresent(attack -> {
						attacks.add(attack);
						remainingDefenders.remove(attack.getDefender());
					});
		}
	}

	public boolean isFinished() {
		return immuneSystem.getGroups().isEmpty() || infection.getGroups().isEmpty();
	}
}
