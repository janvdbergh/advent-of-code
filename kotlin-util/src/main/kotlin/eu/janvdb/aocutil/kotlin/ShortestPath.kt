package eu.janvdb.aocutil.kotlin

import java.util.*

fun <P> findShortestPath(
	start: P,
	endFunction: (P) -> Boolean,
	neighboursFunction: (P) -> Sequence<ShortestPathMove<P>>,
	expectedCostRemainingFunction: (P) -> Int,
	maxLength: Int = Int.MAX_VALUE
): ShortestPathState<P>? {
	val comparator = Comparator.comparingInt<ShortestPathState<P>> {
		val x = expectedCostRemainingFunction.invoke(it.state)
		it.cost + x
	}
	val openList = PriorityQueue(comparator)

	return findShortestPaths(start, endFunction, openList, neighboursFunction, false, maxLength).firstOrNull()
}

fun <P> findShortestPath(
	start: P,
	end: P,
	neighboursFunction: (P) -> Sequence<ShortestPathMove<P>>,
	maxLength: Int = Int.MAX_VALUE
): ShortestPathState<P>? {
	val openList = LinkedList<ShortestPathState<P>>()
	return findShortestPaths(start, { it: P -> it == end }, openList, neighboursFunction, false, maxLength).firstOrNull()
}

fun <P> findShortestPath(
	start: P,
	endFunction: (P) -> Boolean,
	neighboursFunction: (P) -> Sequence<ShortestPathMove<P>>,
	maxLength: Int = Int.MAX_VALUE

): ShortestPathState<P>? {
	val openList = LinkedList<ShortestPathState<P>>()
	return findShortestPaths(start, endFunction, openList, neighboursFunction, false, maxLength).firstOrNull()
}

fun <P> findShortestPaths(
	start: P,
	endFunction: (P) -> Boolean,
	neighboursFunction: (P) -> Sequence<ShortestPathMove<P>>,
	maxLength: Int = Int.MAX_VALUE
): List<ShortestPathState<P>> {
	val openList = LinkedList<ShortestPathState<P>>()
	return findShortestPaths(start, endFunction, openList, neighboursFunction, true, maxLength)
}

private const val LARGE_VALUE = Int.MAX_VALUE / 2

private fun <P> findShortestPaths(
	start: P,
	endFunction: (P) -> Boolean,
	pointsToVisit: Queue<ShortestPathState<P>>,
	neighboursFunction: (P) -> Sequence<ShortestPathMove<P>>,
	collectAllResults: Boolean,
	maxLength: Int
): List<ShortestPathState<P>> {
	if (endFunction.invoke(start)) return listOf(ShortestPathState(null, start, 0))

	val bestMap = mutableMapOf(Pair(start, 0))
	val result = mutableListOf<ShortestPathState<P>>()
	pointsToVisit.add(ShortestPathState(null, start, 0))

	fun getCurrentResultCost() = result.firstOrNull()?.cost ?: LARGE_VALUE

	while (!pointsToVisit.isEmpty()) {
		val current = pointsToVisit.remove()
		val (_, currentPoint, currentCost) = current

		if (currentCost >= getCurrentResultCost()) continue

		neighboursFunction.invoke(currentPoint).forEach { (point, cost) ->
			val currentBest = bestMap.getOrDefault(point, LARGE_VALUE)
			val newCost = currentCost + cost
			val currentResultCost = getCurrentResultCost()

			if (newCost <= maxLength &&
				((newCost < currentBest) || (collectAllResults && newCost == currentBest)) &&
				((newCost < currentResultCost) || (collectAllResults && newCost == currentResultCost))
			) {
				bestMap[point] = newCost
				val newState = ShortestPathState(current, point, newCost)
				pointsToVisit.add(newState)
				if (endFunction.invoke(point)) {
					if (newCost < currentResultCost) {
						result.clear()
						result += newState
					} else {
						if (collectAllResults) result += newState
					}
					pointsToVisit.removeIf { it.cost >= newCost }
				}
			}
		}
	}

	return result
}

data class ShortestPathMove<P>(val nextState: P, val cost: Int)

data class ShortestPathState<P>(val previous: ShortestPathState<P>?, val state: P, val cost: Int) {

	fun getStates(): Sequence<P> {
		return getStatesWithCost().map { it.first }
	}

	fun getStatesWithCost(): Sequence<Pair<P, Int>> {
		return sequence {
			var current: ShortestPathState<P>? = this@ShortestPathState
			while(current != null) {
				yield(Pair(current.state, current.cost))
				current = current.previous
			}
		}
	}

	override fun toString(): String {
		val str = "${state} ($cost)"
		if (previous == null) return str
		return "$previous -> $str"
	}
}
