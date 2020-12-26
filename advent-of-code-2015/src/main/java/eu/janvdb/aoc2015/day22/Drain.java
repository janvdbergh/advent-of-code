package eu.janvdb.aoc2015.day22;

public class Drain extends MagicItem {

	private static final int COST = 73;
	private static final int DAMAGE = 2;

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
				.forEach(x -> {
					gameState.removeBossHitPoints(DAMAGE);
					gameState.addHeroHitPoints(DAMAGE);
				});
	}
}
