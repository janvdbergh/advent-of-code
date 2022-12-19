package eu.janvdb.aoc2022.day19

val BLUEPRINT_PATTERN = Regex(
	"Blueprint (\\d+): " +
			"Each ore robot costs (\\d+) ore. " +
			"Each clay robot costs (\\d+) ore. " +
			"Each obsidian robot costs (\\d+) ore and (\\d+) clay. " +
			"Each geode robot costs (\\d+) ore and (\\d+) obsidian."
)

data class Blueprint(
	val number: Int,
	val oreRecipe: Materials,
	val clayRecipe: Materials,
	val obsidianRecipe: Materials,
	val geodeRecipe: Materials
)

fun String.toBluePrint(): Blueprint {
	val group = BLUEPRINT_PATTERN.matchEntire(this) ?: throw IllegalArgumentException(this)
	val values = group.groupValues

	return Blueprint(
		values[1].toInt(),
		Materials(values[2].toInt(), 0, 0, 0),
		Materials(values[3].toInt(), 0, 0, 0),
		Materials(values[4].toInt(), values[5].toInt(), 0, 0),
		Materials(values[6].toInt(), 0, values[7].toInt(), 0)
	)
}