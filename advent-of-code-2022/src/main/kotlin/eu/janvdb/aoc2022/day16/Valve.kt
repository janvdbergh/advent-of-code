package eu.janvdb.aoc2022.day16

import eu.janvdb.aocutil.kotlin.Move
import eu.janvdb.aocutil.kotlin.findShortestPath

const val START_VALVE = "AA"

val PATTERN = Regex("Valve ([A-Z]+) has flow rate=(\\d+); tunnels? leads? to valves? ([A-Z, ]+)")
val SPLIT = Regex(", ")

data class Valve(val name: String, val flowRate: Int, val leadsTo: List<String>)

fun String.toValve(): Valve {
	val match = PATTERN.matchEntire(this) ?: throw IllegalArgumentException(this)

	val name = match.groupValues[1]
	val flowRate = match.groupValues[2].toInt()
	val leadsTo = match.groupValues[3].split(SPLIT).toList()

	return Valve(name, flowRate, leadsTo)
}

data class Valves(val valvesMap: Map<String, Valve>, val distances: Map<String, Map<String, Int>>) {
	fun stepLength(from: String, to: String): Int {
		return distances[from]!![to]!!
	}

	fun flowRate(it: String): Int {
		return valvesMap[it]!!.flowRate
	}

	val names = distances.keys

	companion object {
		fun create(valves: List<Valve>): Valves {
			val valvesMap = valves.associateBy { it.name }
			val valvesToConsider = valves.filter { it.flowRate != 0 } + valvesMap[START_VALVE]!!

			fun subMap(valve1: Valve): Pair<String, Map<String, Int>> {
				val subMap = valvesToConsider.asSequence()
					.filter { it != valve1 }
					.map { valve2 ->
						val shortestPath = findShortestPath(valve1, valve2) { valve ->
							valve.leadsTo.asSequence().map { Move(valvesMap[it]!!, 1) }
						}
						Pair(valve2.name, shortestPath!!)
					}
					.toMap()
				return Pair(valve1.name, subMap)
			}

			val distances = valvesToConsider.asSequence()
				.map { subMap(it) }
				.toMap()

			return Valves(valvesMap, distances)
		}
	}
}