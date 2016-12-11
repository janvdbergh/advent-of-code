package eu.janvdb.aoc2015.day22;

public class Poison extends DurableMagicItem {

	private static final int COST = 173;
	private static final int DURATION = 6;
	private static final int DAMAGE = 3;

	@Override
	public int getCost() {
		return COST;
	}

	@Override
	public int getDuration() {
		return DURATION;
	}

	@Override
	public void doActionAtStartIfActive(GameState gameState) {
		gameState.removeBossHitPoints(DAMAGE);
	}

	@Override
	public void doActionAtEndIfActive(GameState gameState) {
	}
}
