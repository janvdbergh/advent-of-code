package eu.janvdb.aoc2015.day22;

public class Shield extends DurableMagicItem {

	private static final int COST = 113;
	private static final int DURATION = 6;
	private static final int ARMOR = 7;

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
		gameState.addHeroArmor(ARMOR);
	}

	@Override
	public void doActionAtEndIfActive(GameState gameState) {
	}
}
