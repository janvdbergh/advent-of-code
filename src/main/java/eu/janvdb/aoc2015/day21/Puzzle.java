package eu.janvdb.aoc2015.day21;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

public class Puzzle {

	private static final int BOSS_HIT_POINTS = 103;
	private static final int BOSS_DAMAGE = 9;
	private static final int BOSS_ARMOR = 2;

	private static final int HERO_HIT_POINTS = 100;

	private static final List<Item> WEAPONS = Arrays.asList(
			new Item("Dagger", 8, 4, 0),
			new Item("Shortsword", 10, 5, 0),
			new Item("Warhammer", 25, 6, 0),
			new Item("Longsword", 40, 7, 0),
			new Item("Greataxe", 74, 8, 0)
	);
	private static final List<Item> ARMORS = Arrays.asList(
			new Item("None", 0, 0, 0),
			new Item("Leather", 13, 0, 1),
			new Item("Chainmail", 31, 0, 2),
			new Item("Splintmail", 53, 0, 3),
			new Item("Bandedmail", 75, 0, 4),
			new Item("Platemail", 102, 0, 5)
	);
	private static final List<Item> RINGS = Arrays.asList(
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
		List<CombinedItem> combinedItems = new ArrayList<>();
		WEAPONS.forEach(weapon ->
				ARMORS.forEach(armor ->
						RINGS.forEach(ring1 ->
								RINGS.stream()
								.filter(ring2 -> ring2 != ring1)
								.forEach(ring2 -> {
									combinedItems.add(new CombinedItem(weapon, armor, ring1, ring2));
								})
						)
				)
		);

		combinedItems.stream()
				.sorted(Comparator.comparing(CombinedItem::getCost).reversed())
				.filter(combinedItem -> !combinedItem.canWin())
				.findFirst()
				.ifPresent(System.out::println);
	}

	private static class Item {
		private final String name;
		private final int cost;
		private final int damage;
		private final int armor;

		public Item(String name, int cost, int damage, int armor) {
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

		public CombinedItem(Item ...items) {
			this.items = Arrays.asList(items);
		}

		public String getName() {
			return items.stream()
					.map(Item::getName)
					.collect(Collectors.joining("/"));
		}

		public int getCost() {
			return doSum(Item::getCost);
		}

		public int getDamage() {
			return doSum(Item::getDamage);

		}

		public int getArmor() {
			return doSum(Item::getArmor);
		}

		@Override
		public String toString() {
			return getName() + ": " + getCost();
		}

		public boolean canWin() {
			int heroLoss = Math.max(1, BOSS_DAMAGE - getArmor());
			int bossLoss = Math.max(1, getDamage() - BOSS_ARMOR);

			int heroTurnsToWin = (BOSS_HIT_POINTS + bossLoss - 1) / bossLoss;
			int bossTurnsToWin = (HERO_HIT_POINTS + heroLoss - 1) / heroLoss;

			return heroTurnsToWin <= bossTurnsToWin;
		}

		private int doSum(ToIntFunction<Item> extractor) {
			return items.stream()
					.mapToInt(extractor)
					.sum();
		}
	}
}
