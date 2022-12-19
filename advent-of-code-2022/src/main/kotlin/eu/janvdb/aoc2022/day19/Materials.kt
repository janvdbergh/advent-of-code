package eu.janvdb.aoc2022.day19

data class Materials(val ore: Int, val clay: Int, val obsidian: Int, val geodes: Int) {
	operator fun plus(other: Materials): Materials {
		return Materials(ore + other.ore, clay + other.clay, obsidian + other.obsidian, geodes + other.geodes)
	}

	operator fun minus(other: Materials): Materials {
		return Materials(ore - other.ore, clay - other.clay, obsidian - other.obsidian, geodes - other.geodes)
	}

	fun canBeBuiltFrom(other: Materials): Boolean =
		ore <= other.ore && clay <= other.clay && obsidian <= other.obsidian && geodes <= other.geodes

	override fun toString(): String {
		return "(ore=$ore, clay=$clay, obsidian=$obsidian, geodes=$geodes)"
	}

	companion object {
		val ONE_ORE = Materials(1, 0, 0, 0)
		val ONE_CLAY = Materials(0, 1, 0, 0)
		val ONE_OBSIDIAN = Materials(0, 0, 1, 0)
		val ONE_GEODE = Materials(0, 0, 0, 1)
	}
}