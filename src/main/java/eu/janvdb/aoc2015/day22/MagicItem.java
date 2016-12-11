package eu.janvdb.aoc2015.day22;

public abstract class MagicItem {

	public abstract int getCost();

	public boolean canBeCast(GameState gameState) {
		return gameState.getHeroManna() >= getCost();
	}

	public void cast(GameState gameState) {
		if (!canBeCast(gameState)) {
			throw new IllegalStateException();
		}
		gameState.removeHeroManna(getCost());
		gameState.markMagicItemCast(this);
	}

	public abstract void doActionAtStart(GameState gameState);

	public abstract void doActionAtEnd(GameState gameState);
}
