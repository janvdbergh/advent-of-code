package eu.janvdb.aoc2025.day11

import eu.janvdb.aocutil.kotlin.readLines

//const val FILENAME = "input11-test.txt"
//const val FILENAME = "input11-test-2.txt"
const val FILENAME = "input11.txt"

const val OUT = "out"

fun main() {
    val links = readLines(2025, FILENAME).map {
        val parts = it.split(": ")
        val destinations = parts[1].split(" ")
        Pair(parts[0], destinations)
    }.toMap()

    val pathfinder = PathFinder(links)
    println(pathfinder.pathsToOut["you"]?.number)
    println(pathfinder.pathsToOut["svr"]?.numberWithBoth)
}

class PathFinder(val connections: Map<String, List<String>>) {
    val pathsToOut = countPathsToOut();

    private fun countPathsToOut(): Map<String, PathCount> {
        val result = mutableMapOf(Pair(OUT, PathCount(1, 0, 0, 0)))
        val toDo = (connections.keys - OUT).toMutableSet()

        while (toDo.isNotEmpty()) {
            val next = toDo.find { result.keys.containsAll(connections[it]!!) }
            if (next == null) throw Exception("No solution")

            val pathCounts = connections[next]!!.map { result[it]!! }
            val newPathCount = PathCount.sum(pathCounts, next)
            result[next] = newPathCount
            toDo -= next
        }

        return result
    }
}

data class PathCount(val number: Long, val numberWithFft: Long, val numberWithDac: Long, val numberWithBoth: Long) {
    companion object {
        fun sum(counts: Collection<PathCount>, nextNode: String): PathCount {
            val numberSum = counts.sumOf { it.number }
            val numberWithFftSum = counts.sumOf { it.numberWithFft }
            val numberWithDacSum = counts.sumOf { it.numberWithDac }
            val numberWithBothSum = counts.sumOf { it.numberWithBoth }

            return PathCount(
                numberSum,
                if (nextNode == "fft") numberSum else numberWithFftSum,
                if (nextNode == "dac") numberSum else numberWithDacSum,
                if (nextNode == "fft") numberWithDacSum else if (nextNode == "dac") numberWithFftSum else numberWithBothSum
            )
        }
    }
}
