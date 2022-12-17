package eu.janvdb.aoc2022.day17

enum class Jet { LEFT, RIGHT }

fun List<String>.toJets(): List<Jet> {
	val jets = this.flatMap { it.toCharArray().asSequence() }
		.map {
			when (it) {
				'<' -> Jet.LEFT
				'>' -> Jet.RIGHT
				else -> throw IllegalArgumentException("Invalid: $it")
			}
		}
	return jets
}