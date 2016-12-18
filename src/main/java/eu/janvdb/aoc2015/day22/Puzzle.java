package eu.janvdb.aoc2015.day22;

import javaslang.collection.List;
import javaslang.control.Option;

public class Puzzle {

	private static final MagicMissile MAGIC_MISSILE = new MagicMissile();
	private static final Drain DRAIN = new Drain();
	private static final Shield SHIELD = new Shield();
	private static final Poison POISON = new Poison();
	private static final Recharge RECHARGE = new Recharge();

	private static final List<MagicItem> MAGIC_ITEMS = List.of(
			MAGIC_MISSILE,
			DRAIN,
			SHIELD,
			POISON,
			RECHARGE
	);

	private GameState gameStateWithLeastMannaUsed = null;

	public static void main(String[] args) {
		new Puzzle().execute();
	}

	private void execute() {
		List<GameState> gameStates = List.of(new GameState());

		while (!gameStates.isEmpty()) {
			GameState gameState = gameStates.last();
			gameStates = gameStates.remove(gameState);

			if (gameStateWithLeastMannaUsed != null && gameState.getTotalMannaUsed() >= gameStateWithLeastMannaUsed.getTotalMannaUsed()) {
				continue;
			}

			gameState.removeHeroHitPoints(1);
			if (gameState.hasBossWon()) {
				continue;
			}

			gameState.startTurn();
			gameStates = gameStates.appendAll(MAGIC_ITEMS.toStream()
					.filter(item -> item.canBeCast(gameState))
					.flatMap(item -> {
						GameState newGameState = new GameState(gameState);
						item.cast(newGameState);
						newGameState.endTurn();
						newGameState.startTurn();

						if (newGameState.hasHeroWon()) {
							updateLeastMannaUsed(newGameState);
						} else {
							newGameState.performBossAttack();
							newGameState.endTurn();
							if (!newGameState.hasBossWon()) {
								return Option.of(newGameState);
							}
						}

						return Option.none();
					})
			);
		}

		if (gameStateWithLeastMannaUsed != null) {
			System.out.println(gameStateWithLeastMannaUsed.getTotalMannaUsed());
			gameStateWithLeastMannaUsed.getMagicCast()
					.forEach(item -> System.out.println(item.getClass().getName()));

			List<String> output = List.empty();
			GameState gameState = gameStateWithLeastMannaUsed;
			while(gameState!=null) {
				output = output.insert(0, gameState.toString());
				gameState = gameState.getPreviousGameState();
			}
			output.forEach(System.out::println);
		}
	}

	private void updateLeastMannaUsed(GameState gameState) {
		if (gameStateWithLeastMannaUsed == null || gameState.getTotalMannaUsed() < gameStateWithLeastMannaUsed.getTotalMannaUsed()) {
			gameStateWithLeastMannaUsed = gameState;
		}
	}

}
