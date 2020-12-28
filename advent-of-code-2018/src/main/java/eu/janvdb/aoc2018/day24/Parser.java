package eu.janvdb.aoc2018.day24;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Parser {
	private static final Pattern UNITS_REGEX = Pattern.compile("(\\d+) units");
	private static final Pattern HIT_POINTS_REGEX = Pattern.compile("(\\d+) hit points");
	private static final Pattern WEAKNESSES_REGEX = Pattern.compile("weak to ([^;)]+)");
	private static final Pattern IMMUNITIES_REGEX = Pattern.compile("immune to ([^;)]+)");
	private static final Pattern DAMAGE_REGEX = Pattern.compile("(\\d+) ([a-z]+) damage");
	private static final Pattern INITIATIVE_REGEX = Pattern.compile("initiative (\\d+)");

	public static AttackSystem parseSystem(List<String> lines) {
		String name = lines.get(0).replaceAll(":.*", "");
		List<Group> groups = IntStream.range(1, lines.size())
				.mapToObj(index -> parseGroup(String.format("%s %d", name, index), lines.get(index)))
				.collect(Collectors.toList());
		return new AttackSystem(name, groups);
	}

	public static Group parseGroup(String groupName, String description) {
		Matcher unitsMatcher = UNITS_REGEX.matcher(description);
		if (!unitsMatcher.find()) throw new IllegalArgumentException(description);
		int numberOfUnits = Integer.parseInt(unitsMatcher.group(1));

		Matcher hitPointsMatcher = HIT_POINTS_REGEX.matcher(description);
		if (!hitPointsMatcher.find()) throw new IllegalArgumentException(description);
		int hitPoints = Integer.parseInt(hitPointsMatcher.group(1));

		Set<String> weaknesses = Collections.emptySet();
		Matcher weaknessesMatcher = WEAKNESSES_REGEX.matcher(description);
		if (weaknessesMatcher.find()) {
			weaknesses = Set.of(weaknessesMatcher.group(1).split(", "));
		}

		Set<String> immunities = Collections.emptySet();
		Matcher immunityMatcher = IMMUNITIES_REGEX.matcher(description);
		if (immunityMatcher.find()) {
			immunities = Set.of(immunityMatcher.group(1).split(", "));
		}

		Matcher damageMatcher = DAMAGE_REGEX.matcher(description);
		if (!damageMatcher.find()) throw new IllegalArgumentException(description);
		int attackDamage = Integer.parseInt(damageMatcher.group(1));
		String attackType = damageMatcher.group(2);

		Matcher initiativeMatcher = INITIATIVE_REGEX.matcher(description);
		if (!initiativeMatcher.find()) throw new IllegalArgumentException(description);
		int initiative = Integer.parseInt(initiativeMatcher.group(1));

		return new Group(groupName, numberOfUnits, hitPoints, immunities, weaknesses, attackType, attackDamage, initiative);
	}
}
