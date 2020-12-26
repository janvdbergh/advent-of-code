package eu.janvdb.aoc2015.day22;

public class Recharge extends DurableMagicItem {

	private static final int COST = 229;
	private static final int DURATION = 5;
	private static final int RECHARGE_AMOUNT = 101;

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
		gameState.addHeroManna(RECHARGE_AMOUNT);
	}

	@Override
	public void doActionAtEnd(GameState gameState) {
	}
}
