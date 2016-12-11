package eu.janvdb.aoc2015.day22;

public class MagicMissile extends MagicItem {

	private static final int COST = 53;
	private static final int DAMAGE = 4;

	@Override
	public int getCost() {
		return COST;
	}

	@Override
	public void doActionAtStart(GameState gameState) {
	}

	@Override
	public void doActionAtEnd(GameState gameState) {
		gameState.getTurnsSinceMagicItemCast(this)
				.filter(numberOfTurns -> numberOfTurns == 0)
				.ifPresent(x -> gameState.removeBossHitPoints(DAMAGE));
	}
}
