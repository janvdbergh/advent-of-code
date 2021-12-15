package eu.janvdb.aoc2021.day15

import java.util.*

fun <P> findShortestPath(start: P, end: P, neighboursFunction: (P) -> Sequence<Pair<P, Int>>): Int? {
	val bestMap = mutableMapOf<P, Int>()
	bestMap.put(start, 0)

	val openList = PriorityQueue<Pair<P, Int>>(Comparator.comparingInt { it.second })
	openList.add(Pair(start, 0))

	while (!openList.isEmpty()) {
		val (currentPoint, currentCost) = openList.remove()

		neighboursFunction.invoke(currentPoint).forEach { (point, cost) ->
			val currentBest = bestMap.getOrDefault(point, Int.MAX_VALUE / 2)
			val newCost = currentCost + cost
			if (newCost < currentBest) {
				bestMap.put(point, newCost)
				openList.add(Pair(point, newCost))
			}
		}
	}

	return bestMap.get(end)
}