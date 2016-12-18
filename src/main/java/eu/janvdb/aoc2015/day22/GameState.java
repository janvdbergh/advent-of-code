package eu.janvdb.aoc2015.day22;

import javaslang.collection.HashMap;
import javaslang.collection.List;
import javaslang.collection.Map;
import javaslang.control.Option;

public class GameState implements Cloneable {

	private static final int BOSS_DAMAGE = 8;
	private static final int HERO_MANNA = 500;
	private static final int HERO_HITPOINTS = 50;
	private static final int BOSS_HITPOINTS = 55;

	private int turn;
	private int heroManna;
	private int totalMannaUsed;
	private int heroArmor;
	private int heroHitPoints;
	private int bossHitPoints;
	private Map<MagicItem, Integer> magicItemLastCast;
	private List<MagicItem> magicCast;
	private GameState previousGameState;

	public GameState() {
		this.turn = 0;
		this.heroManna = HERO_MANNA;
		this.totalMannaUsed = 0;
		this.heroArmor = 0;
		this.heroHitPoints = HERO_HITPOINTS;
		this.bossHitPoints = BOSS_HITPOINTS;
		this.magicItemLastCast = HashMap.empty();
		this.magicCast = List.empty();
	}

	public GameState(GameState previousGameState) {
		this.turn = previousGameState.turn;
		this.heroManna = previousGameState.heroManna;
		this.totalMannaUsed = previousGameState.totalMannaUsed;
		this.heroArmor = previousGameState.heroArmor;
		this.heroHitPoints = previousGameState.heroHitPoints;
		this.bossHitPoints = previousGameState.bossHitPoints;
		this.magicItemLastCast = previousGameState.magicItemLastCast;
		this.magicCast = previousGameState.magicCast;

		this.previousGameState = previousGameState;
	}

	public void startTurn() {
		turn++;
		heroArmor = 0;
		magicItemLastCast.keySet().forEach(magicItem -> magicItem.doActionAtStart(this));
	}

	public void endTurn() {
		magicItemLastCast.keySet().forEach(magicItem -> magicItem.doActionAtEnd(this));
	}

	public int getHeroManna() {
		return heroManna;
	}

	public int getTotalMannaUsed() {
		return totalMannaUsed;
	}

	public void removeHeroManna(int amount) {
		heroManna -= amount;
		totalMannaUsed += amount;
		if (heroManna < 0) {
			throw new IllegalStateException();
		}
	}

	public void addHeroManna(int amount) {
		heroManna += amount;
	}

	public void addHeroHitPoints(int amount) {
		heroHitPoints += amount;
	}

	public void removeHeroHitPoints(int amount) {
		heroHitPoints -= amount;
	}

	public void performBossAttack() {
		heroHitPoints -= Math.max(1, BOSS_DAMAGE - heroArmor);
	}

	public boolean hasBossWon() {
		return heroHitPoints <= 0;
	}

	public void addHeroArmor(int amount) {
		heroArmor += amount;
	}

	public void removeBossHitPoints(int amount) {
		bossHitPoints -= amount;
	}

	public boolean hasHeroWon() {
		return bossHitPoints <= 0;
	}

	public void markMagicItemCast(MagicItem magicItem) {
		magicItemLastCast = magicItemLastCast.put(magicItem, turn);
		magicCast = magicCast.append(magicItem);
	}

	public Option<Integer> getTurnsSinceMagicItemCast(MagicItem magicItem) {
		return magicItemLastCast
				.get(magicItem)
				.map(result -> turn - result);
	}

	@Override
	public GameState clone() {
		try {
			return (GameState) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new IllegalStateException("Should be cloneable");
		}
	}

	@Override
	public String toString() {
		return "At turn " + turn + ":\n" +
				"Player has " + heroHitPoints + " hit points, " + heroArmor + " armor, " + heroManna + " mana.\n" +
				"Boss has " + bossHitPoints + " hit points.\n";
	}

	public List<MagicItem> getMagicCast() {
		return magicCast;
	}

	public GameState getPreviousGameState() {
		return previousGameState;
	}

}

