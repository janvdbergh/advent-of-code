package eu.janvdb.aoc2015.day21;

import javaslang.Function1;
import javaslang.collection.List;

import java.util.Comparator;

public class Puzzle {

	private static final int BOSS_HIT_POINTS = 103;
	private static final int BOSS_DAMAGE = 9;
	private static final int BOSS_ARMOR = 2;

	private static final int HERO_HIT_POINTS = 100;

	private static final List<Item> WEAPONS = List.of(
			new Item("Dagger", 8, 4, 0),
			new Item("Shortsword", 10, 5, 0),
			new Item("Warhammer", 25, 6, 0),
			new Item("Longsword", 40, 7, 0),
			new Item("Greataxe", 74, 8, 0)
	);
	private static final List<Item> ARMORS = List.of(
			new Item("None", 0, 0, 0),
			new Item("Leather", 13, 0, 1),
			new Item("Chainmail", 31, 0, 2),
			new Item("Splintmail", 53, 0, 3),
			new Item("Bandedmail", 75, 0, 4),
			new Item("Platemail", 102, 0, 5)
	);
	private static final List<Item> RINGS = List.of(
			new Item("None", 0, 0, 0),
			new Item("None", 0, 0, 0),
			new Item("Ring Damage +1", 25, 1, 0),
			new Item("Ring Damage +2", 50, 2, 0),
			new Item("Ring Damage +3", 100, 3, 0),
			new Item("Ring Defense +1", 20, 0, 1),
			new Item("Ring Defense +2", 40, 0, 2),
			new Item("Ring Defense +3", 80, 0, 3)
	);

	public static void main(String[] args) {
		new Puzzle().execute();
	}

	private void execute() {
		List<CombinedItem> combinedItems = WEAPONS.flatMap(
						weapon -> ARMORS.flatMap(
								armor -> RINGS.flatMap(
										ring1 -> RINGS.filter(ring2 -> ring2 != ring1)
												.map(ring2 -> new CombinedItem(weapon, armor, ring1, ring2)
												)
								)
						)
				)
				.sorted(Comparator.comparing(CombinedItem::getCost).reversed());

		combinedItems.toStream()
				.find(combinedItem -> !combinedItem.canWin())
				.forEach(System.out::println);
	}

	private static class Item {
		private final String name;
		private final int cost;
		private final int damage;
		private final int armor;

		Item(String name, int cost, int damage, int armor) {
			this.name = name;
			this.cost = cost;
			this.damage = damage;
			this.armor = armor;
		}

		public String getName() {
			return name;
		}

		public int getCost() {
			return cost;
		}

		public int getDamage() {
			return damage;
		}

		public int getArmor() {
			return armor;
		}
	}

	private static class CombinedItem {
		private final List<Item> items;

		CombinedItem(Item... items) {
			this.items = List.of(items);
		}

		public String getName() {
			return items.toStream()
					.map(Item::getName)
					.mkString("/");
		}

		int getCost() {
			return doSum(Item::getCost);
		}

		int getDamage() {
			return doSum(Item::getDamage);

		}

		int getArmor() {
			return doSum(Item::getArmor);
		}

		@Override
		public String toString() {
			return getName() + ": " + getCost();
		}

		boolean canWin() {
			int heroLoss = Math.max(1, BOSS_DAMAGE - getArmor());
			int bossLoss = Math.max(1, getDamage() - BOSS_ARMOR);

			int heroTurnsToWin = (BOSS_HIT_POINTS + bossLoss - 1) / bossLoss;
			int bossTurnsToWin = (HERO_HIT_POINTS + heroLoss - 1) / heroLoss;

			return heroTurnsToWin <= bossTurnsToWin;
		}

		private int doSum(Function1<Item, Integer> extractor) {
			return items.toStream()
					.map(extractor)
					.sum()
					.intValue();
		}
	}
}
