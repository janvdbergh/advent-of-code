package eu.janvdb.aoc2018.day15;

enum ActorType {
	ELF(3, 200),
	GOBLIN(3, 200);

	private final int initialHitPoints;
	private int attackPower;

	ActorType(int attackPower, int initialHitPoints) {
		this.attackPower = attackPower;
		this.initialHitPoints = initialHitPoints;
	}

	public int getAttackPower() {
		return attackPower;
	}

	public int getInitialHitPoints() {
		return initialHitPoints;
	}

	public void setAttackPower(int attackPower) {
		this.attackPower = attackPower;
	}
}
