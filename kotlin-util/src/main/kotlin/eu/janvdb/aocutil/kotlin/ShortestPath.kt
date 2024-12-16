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

	return findShortestPaths(start, endFunction, openList, neighboursFunction, false).firstOrNull()
}

fun <P> findShortestPath(
	start: P,
	end: P,
	neighboursFunction: (P) -> Sequence<ShortestPathMove<P>>
): ShortestPathState<P>? {
	val openList = LinkedList<ShortestPathState<P>>()
	return findShortestPaths(start, { it: P -> it == end }, openList, neighboursFunction, false).firstOrNull()
}

fun <P> findShortestPath(
	start: P,
	endFunction: (P) -> Boolean,
	neighboursFunction: (P) -> Sequence<ShortestPathMove<P>>
): ShortestPathState<P>? {
	val openList = LinkedList<ShortestPathState<P>>()
	return findShortestPaths(start, endFunction, openList, neighboursFunction, false).firstOrNull()
}

fun <P> findShortestPaths(
	start: P,
	endFunction: (P) -> Boolean,
	neighboursFunction: (P) -> Sequence<ShortestPathMove<P>>
): List<ShortestPathState<P>> {
	val openList = LinkedList<ShortestPathState<P>>()
	return findShortestPaths(start, endFunction, openList, neighboursFunction, true)
}

private const val LARGE_VALUE = Int.MAX_VALUE / 2

private fun <P> findShortestPaths(
	start: P,
	endFunction: (P) -> Boolean,
	pointsToVisit: Queue<ShortestPathState<P>>,
	neighboursFunction: (P) -> Sequence<ShortestPathMove<P>>,
	collectAllResults: Boolean
): List<ShortestPathState<P>> {
	val bestMap = mutableMapOf(Pair(start, 0))
	val result = mutableListOf<ShortestPathState<P>>()
	pointsToVisit.add(ShortestPathState(null, start, 0))

	fun currentResultCost() = result.firstOrNull()?.cost ?: LARGE_VALUE

	while (!pointsToVisit.isEmpty()) {
		val current = pointsToVisit.remove()
		val (_, currentPoint, currentCost) = current

		if (currentCost >= currentResultCost()) continue

		neighboursFunction.invoke(currentPoint).forEach { (point, cost) ->
			val currentBest = bestMap.getOrDefault(point, LARGE_VALUE)
			val newCost = currentCost + cost
			if (newCost <= currentBest && newCost <= currentResultCost()) {
				bestMap[point] = newCost
				val newState = ShortestPathState(current, point, newCost)
				pointsToVisit.add(newState)
				if (endFunction.invoke(point)) {
					if (newCost < currentResultCost()) {
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
		return sequence {
			var current: ShortestPathState<P>? = this@ShortestPathState
			while(current != null) {
				yield(current.state)
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
