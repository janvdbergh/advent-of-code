package eu.janvdb.aoc2015.day22;

public abstract class DurableMagicItem extends MagicItem {

	protected abstract int getDuration();

	@Override
	public boolean canBeCast(GameState gameState) {
		return super.canBeCast(gameState) &&
				gameState.getTurnsSinceMagicItemCast(this).getOrElse(getDuration()) >= getDuration();
	}

	@Override
	public void doActionAtStart(GameState gameState) {
		doIfActive(gameState, () -> doActionAtStartIfActive(gameState));
	}

	protected abstract void doActionAtStartIfActive(GameState gameState);

	@Override
	public void doActionAtEnd(GameState gameState) {
		doIfActive(gameState, () -> doActionAtEndIfActive(gameState));
	}

	protected abstract void doActionAtEndIfActive(GameState gameState);

	private void doIfActive(GameState gameState, Runnable action) {
		gameState.getTurnsSinceMagicItemCast(this)
				.filter(numberOfTurns -> numberOfTurns <= getDuration() && numberOfTurns > 0)
				.forEach(x -> action.run());
	}
}
