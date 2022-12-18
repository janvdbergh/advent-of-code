package eu.janvdb.aoc2022.day18

data class Cubes(val cubes: Set<Cube>) {
	val uniqueSides: Set<Side> by lazy {
		cubes
			.flatMap(Cube::getSides)
			.groupingBy { it }
			.eachCount()
			.filterValues { it == 1 }
			.keys
	}

	fun isLinkedTo(other: Cubes) = uniqueSides.intersect(other.uniqueSides).isNotEmpty()

	fun join(other: Cubes) = Cubes(cubes + other.cubes)

	fun findAirPockets(): Cubes {
		fun getAllCubesInRange() = cubes.rangeOver(Cube::x).flatMap { x ->
			cubes.rangeOver(Cube::y).flatMap { y ->
				cubes.rangeOver(Cube::z).map { z -> Cube(x, y, z) }
			}
		}

		fun combineLinkedCubes(unusedCubes: Collection<Cube>): List<Cubes> {
			val possibleAirPockets = unusedCubes.map(::setOf).map(::Cubes).toMutableList()
			val allPockets = mutableListOf<Cubes>()

			while (possibleAirPockets.isNotEmpty()) {
				val possibleAirPocket = possibleAirPockets.removeAt(0)
				val linked = mutableSetOf(possibleAirPocket)
				var linkedResult = possibleAirPocket

				possibleAirPockets.forEach {
					if (linkedResult.isLinkedTo(it)) {
						linked += it
						linkedResult = linkedResult.join(it)
					}
				}

				if (linked.size == 1) {
					allPockets += possibleAirPocket
				} else {
					possibleAirPockets -= linked
					possibleAirPockets += linkedResult
				}
			}

			return allPockets
		}

		val unusedCubes = getAllCubesInRange() - cubes
		val allPockets = combineLinkedCubes(unusedCubes)

		return allPockets
			.filter { it.uniqueSides.minus(uniqueSides).isEmpty() }
			.fold(Cubes(setOf()), Cubes::join)
	}
}

fun Collection<Cube>.toCubes(): Cubes = Cubes(toSet())