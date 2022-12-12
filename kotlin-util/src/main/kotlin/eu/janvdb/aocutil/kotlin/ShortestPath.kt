package eu.janvdb.aocutil.kotlin

import java.util.*

fun <P> findShortestPath(
	start: P,
	end: P,
	neighboursFunction: (P) -> Sequence<Move<P>>,
	expectedCostRemainingFunction: (P) -> Int
): Int? {
	val comparator = Comparator.comparingInt<Pair<P, Int>> {
		val x = expectedCostRemainingFunction.invoke(it.first)
		it.second + x
	}
	val openList = PriorityQueue(comparator)

	return findShortestPath(start, end, openList, neighboursFunction)
}

fun <P> findShortestPath(
	start: P,
	end: P,
	neighboursFunction: (P) -> Sequence<Move<P>>
): Int? {
	val openList = LinkedList<Pair<P, Int>>()
	return findShortestPath(start, end, openList, neighboursFunction)
}

private fun <P> findShortestPath(
	start: P,
	end: P,
	pointsToVisit: Queue<Pair<P, Int>>,
	neighboursFunction: (P) -> Sequence<Move<P>>
): Int? {
	val bestMap = mutableMapOf(Pair(start, 0))
	var result = Int.MAX_VALUE
	pointsToVisit.add(Pair(start, 0))

	while (!pointsToVisit.isEmpty()) {
		val (currentPoint, currentCost) = pointsToVisit.remove()
		if (currentCost >= result) continue

		neighboursFunction.invoke(currentPoint).forEach { (point, cost) ->
			val currentBest = bestMap.getOrDefault(point, Int.MAX_VALUE / 2)
			val newCost = currentCost + cost
			if (newCost < currentBest && newCost < result) {
				bestMap[point] = newCost
				pointsToVisit.add(Pair(point, newCost))
				if (point == end) {
					result = newCost
					pointsToVisit.removeIf { it.second >= newCost }
				}
			}
		}
	}

	return if (result != Int.MAX_VALUE) result else null
}

data class Move<P>(val nextState: P, val cost: Int)