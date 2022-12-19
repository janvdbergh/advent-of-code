package eu.janvdb.aoc2022.day19

class State(
	val minutesLeft: Int,
	val materials: Materials,
	val robots: Materials,
	val blueprint: Blueprint
) {
	val score = ((materials.geodes + robots.geodes * minutesLeft) shl 16) +
			(robots.obsidian shl 8) +
			(robots.clay shl 4) +
			robots.ore

	fun getNextStates(): List<State> {
		val newMaterials = materials + robots
		val newStates = mutableListOf(State(minutesLeft - 1, newMaterials, robots, blueprint))

		if (blueprint.geodeRecipe.canBeBuiltFrom(materials))
			newStates.add(
				State(minutesLeft - 1, newMaterials - blueprint.geodeRecipe, robots + Materials.ONE_GEODE, blueprint)
			)

		if (/*robots.obsidian < blueprint.maxObsidianRobotsNeeded && */blueprint.obsidianRecipe.canBeBuiltFrom(materials))
			newStates.add(
				State(
					minutesLeft - 1, newMaterials - blueprint.obsidianRecipe, robots + Materials.ONE_OBSIDIAN, blueprint
				)
			)

		if (/*robots.clay < blueprint.maxClayRobotsNeeded && */blueprint.clayRecipe.canBeBuiltFrom(materials))
			newStates.add(
				State(minutesLeft - 1, newMaterials - blueprint.clayRecipe, robots + Materials.ONE_CLAY, blueprint)
			)

		if (/*robots.ore < blueprint.maxOreRobotsNeeded && */blueprint.oreRecipe.canBeBuiltFrom(materials))
			newStates.add(
				State(minutesLeft - 1, newMaterials - blueprint.oreRecipe, robots + Materials.ONE_ORE, blueprint)
			)

		return newStates
	}

	override fun toString() =
		"($score) ${blueprint.number} * ${materials.geodes} = ${value()} - $minutesLeft min - $materials - $robots"

	fun value() = blueprint.number * materials.geodes

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as State

		if (minutesLeft != other.minutesLeft) return false
		if (materials != other.materials) return false
		if (robots != other.robots) return false

		return true
	}

	override fun hashCode(): Int {
		var result = minutesLeft
		result = 31 * result + materials.hashCode()
		result = 31 * result + robots.hashCode()
		return result
	}
}