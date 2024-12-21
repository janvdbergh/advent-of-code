package eu.janvdb.aocutil.kotlin

import java.util.*

fun <P, M> findShortestPath(
	start: P,
	endFunction: (P) -> Boolean,
	neighboursFunction: (P) -> Sequence<ShortestPathMove<P, M>>,
	expectedCostRemainingFunction: (P) -> Int,
	maxLength: Int = Int.MAX_VALUE
): ShortestPathState<P, M>? {
	val comparator = Comparator.comparingInt<ShortestPathState<P, M>> {
		val x = expectedCostRemainingFunction.invoke(it.state)
		it.cost + x
	}
	val openList = PriorityQueue(comparator)

	return findShortestPaths(start, endFunction, openList, neighboursFunction, false, maxLength).firstOrNull()
}

fun <P, M> findShortestPath(
	start: P,
	end: P,
	neighboursFunction: (P) -> Sequence<ShortestPathMove<P, M>>,
	maxLength: Int = Int.MAX_VALUE
): ShortestPathState<P, M>? {
	val openList = LinkedList<ShortestPathState<P, M>>()
	return findShortestPaths(start, { it: P -> it == end }, openList, neighboursFunction, false, maxLength).firstOrNull()
}

fun <P, M> findShortestPath(
	start: P,
	endFunction: (P) -> Boolean,
	neighboursFunction: (P) -> Sequence<ShortestPathMove<P, M>>,
	maxLength: Int = Int.MAX_VALUE

): ShortestPathState<P, M>? {
	val openList = LinkedList<ShortestPathState<P, M>>()
	return findShortestPaths(start, endFunction, openList, neighboursFunction, false, maxLength).firstOrNull()
}

fun <P, M> findShortestPaths(
	start: P,
	endFunction: (P) -> Boolean,
	neighboursFunction: (P) -> Sequence<ShortestPathMove<P, M>>,
	maxLength: Int = Int.MAX_VALUE
): List<ShortestPathState<P, M>> {
	val openList = LinkedList<ShortestPathState<P, M>>()
	return findShortestPaths(start, endFunction, openList, neighboursFunction, true, maxLength)
}

fun <P, M> findShortestPaths(
	start: P,
	end: P,
	neighboursFunction: (P) -> Sequence<ShortestPathMove<P, M>>,
	maxLength: Int = Int.MAX_VALUE
): List<ShortestPathState<P, M>> {
	val openList = LinkedList<ShortestPathState<P, M>>()
	return findShortestPaths(start, { it: P -> it == end }, openList, neighboursFunction, true, maxLength)
}

private const val LARGE_VALUE = Int.MAX_VALUE / 2

private fun <P, M> findShortestPaths(
	start: P,
	endFunction: (P) -> Boolean,
	pointsToVisit: Queue<ShortestPathState<P, M>>,
	neighboursFunction: (P) -> Sequence<ShortestPathMove<P, M>>,
	collectAllResults: Boolean,
	maxLength: Int
): List<ShortestPathState<P, M>> {
	if (endFunction.invoke(start)) return listOf(ShortestPathState(null, null, start, 0))

	val bestMap = mutableMapOf(Pair(start, 0))
	val result = mutableListOf<ShortestPathState<P, M>>()
	pointsToVisit.add(ShortestPathState(null, null, start, 0))

	fun getCurrentResultCost() = result.firstOrNull()?.cost ?: LARGE_VALUE

	while (!pointsToVisit.isEmpty()) {
		val current = pointsToVisit.remove()
		val (_, _, currentPoint, currentCost) = current

		if (currentCost >= getCurrentResultCost()) continue

		neighboursFunction.invoke(currentPoint).forEach { (point, move, cost) ->
			val currentBest = bestMap.getOrDefault(point, LARGE_VALUE)
			val newCost = currentCost + cost
			val currentResultCost = getCurrentResultCost()

			if (newCost <= maxLength &&
				((newCost < currentBest) || (collectAllResults && newCost == currentBest)) &&
				((newCost < currentResultCost) || (collectAllResults && newCost == currentResultCost))
			) {
				bestMap[point] = newCost
				val newState = ShortestPathState(current, move, point, newCost)
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

data class ShortestPathMove<P, M>(val nextState: P, val move: M, val cost: Int)

data class ShortestPathState<P, M>(val previous: ShortestPathState<P, M>?, val move: M?, val state: P, val cost: Int) {

	fun getStates(): Sequence<P> {
		return getStatesWithCost().map { it.first }
	}

	fun getMoves(): Sequence<M> {
		return sequence {
			var current: ShortestPathState<P, M>? = this@ShortestPathState
			while (current != null) {
				val move = current.move
				if (move != null) yield(move)
				current = current.previous
			}
		}
	}

	fun getStatesWithCost(): Sequence<Pair<P, Int>> {
		return sequence {
			var current: ShortestPathState<P, M>? = this@ShortestPathState
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
