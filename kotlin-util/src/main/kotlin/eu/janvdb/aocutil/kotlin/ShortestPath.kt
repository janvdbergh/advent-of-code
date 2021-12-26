package eu.janvdb.aocutil.kotlin

import java.util.*


fun <P> findShortestPath(
	start: P,
	end: P,
	neighboursFunction: (P) -> Sequence<Move<P>>,
	expectedCostRemainingFunction: (P) -> Int
): Int? {
	val bestMap = mutableMapOf(Pair(start, 0))
	var result = Int.MAX_VALUE

	val openList = PriorityQueue<Pair<P, Int>>(Comparator.comparingInt {
		val x = expectedCostRemainingFunction.invoke(it.first)
		it.second + x
	})
	openList.add(Pair(start, 0))

	while (!openList.isEmpty()) {
		val (currentPoint, currentCost) = openList.remove()
		if (currentCost >= result) continue

		neighboursFunction.invoke(currentPoint).forEach { (point, cost) ->
			val currentBest = bestMap.getOrDefault(point, Int.MAX_VALUE / 2)
			val newCost = currentCost + cost
			if (newCost < currentBest && newCost < result) {
				bestMap[point] = newCost
				openList.add(Pair(point, newCost))
				if (point == end) {
					result = newCost
					println("??$result")
					openList.removeIf { it.second >= newCost }
				}
			}
		}
	}

	return if (result != Int.MAX_VALUE) result else null
}

data class Move<P>(val nextState: P, val cost: Int)