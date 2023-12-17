package eu.janvdb.aocutil.kotlin

import java.util.*

fun <P> findShortestPath(
	start: P,
	endFunction: (P) -> Boolean,
	neighboursFunction: (P) -> Sequence<ShortestPathMove<P>>,
	expectedCostRemainingFunction: (P) -> Int
): ShortestPathState<P>? {
	val comparator = Comparator.comparingInt<ShortestPathState<P>> {
		val x = expectedCostRemainingFunction.invoke(it.state)
		it.cost + x
	}
	val openList = PriorityQueue(comparator)

	return findShortestPath(start, endFunction, openList, neighboursFunction)
}

fun <P> findShortestPath(
	start: P,
	end: P,
	neighboursFunction: (P) -> Sequence<ShortestPathMove<P>>
): ShortestPathState<P>? {
	val openList = LinkedList<ShortestPathState<P>>()
	return findShortestPath(start, { it == end }, openList, neighboursFunction)
}

fun <P> findShortestPath(
	start: P,
	endFunction: (P) -> Boolean,
	neighboursFunction: (P) -> Sequence<ShortestPathMove<P>>
): ShortestPathState<P>? {
	val openList = LinkedList<ShortestPathState<P>>()
	return findShortestPath(start, endFunction, openList, neighboursFunction)
}

private const val LARGE_VALUE = Int.MAX_VALUE / 2

private fun <P> findShortestPath(
	start: P,
	endFunction: (P) -> Boolean,
	pointsToVisit: Queue<ShortestPathState<P>>,
	neighboursFunction: (P) -> Sequence<ShortestPathMove<P>>
): ShortestPathState<P>? {
	val bestMap = mutableMapOf(Pair(start, 0))
	var result: ShortestPathState<P>? = null
	pointsToVisit.add(ShortestPathState(null, start, 0))

	while (!pointsToVisit.isEmpty()) {
		val current = pointsToVisit.remove()
		val (_, currentPoint, currentCost) = current

		if (result != null && currentCost >= result.cost) continue

		neighboursFunction.invoke(currentPoint).forEach { (point, cost) ->
			val currentBest = bestMap.getOrDefault(point, LARGE_VALUE)
			val newCost = currentCost + cost
			if (newCost < currentBest && newCost < (result?.cost ?: (LARGE_VALUE))) {
				bestMap[point] = newCost
				val newState = ShortestPathState(current, point, newCost)
				pointsToVisit.add(newState)
				if (endFunction.invoke(point)) {
					result = newState
					pointsToVisit.removeIf { it.cost >= newCost }
				}
			}
		}
	}

	return result
}

data class ShortestPathMove<P>(val nextState: P, val cost: Int)

data class ShortestPathState<P>(val previous: ShortestPathState<P>?, val state: P, val cost: Int) {
	override fun toString(): String {
		val str = "${state} ($cost)"
		if (previous == null) return str
		return "$previous -> $str"
	}
}