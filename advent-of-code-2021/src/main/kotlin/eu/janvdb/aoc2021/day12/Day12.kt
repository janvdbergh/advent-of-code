package eu.janvdb.aoc2021.day12

import eu.janvdb.aocutil.kotlin.readLines
import java.util.*

const val FILENAME = "input12.txt"

fun main() {
	val connections = readLines(2021, FILENAME)
		.map { it.split('-').map(::Room) }
		.flatMap { rooms -> listOf(Connection(rooms[0], rooms[1]), Connection(rooms[1], rooms[0])) }

	val paths1 = enumeratePaths(connections, ::canVisit1)
	println(paths1.size)

	val paths2 = enumeratePaths(connections, ::canVisit2)
	println(paths2.size)
}

fun canVisit1(currentPath: Path, room: Room): Boolean {
	return room.isLarge() || !currentPath.contains(room)
}

fun canVisit2(currentPath: Path, room: Room): Boolean {
	if (!currentPath.contains(room)) return true
	if (room.isLarge()) return true
	if (room == Room.START || room == Room.END) return false

	return currentPath.containsSmallRoomTwice()
}

fun enumeratePaths(connections: Collection<Connection>, canVisitFunction: (Path, Room) -> Boolean): List<Path> {
	val result = mutableListOf<Path>()
	val pathsToVisit: Deque<Path> = LinkedList()
	pathsToVisit.addFirst(Path(Room.START))

	while (!pathsToVisit.isEmpty()) {
		val pathToVisit = pathsToVisit.removeFirst()

		val endPoint = pathToVisit.last()
		if (endPoint == Room.END) {
			result.add(pathToVisit)
		} else {
			connections.asSequence()
				.filter { it.from == endPoint }
				.filter { canVisitFunction(pathToVisit, it.to) }
				.forEach {
					val newPath = pathToVisit.add(it.to)
					pathsToVisit.addFirst(newPath)
				}
		}
	}

	return result
}

data class Room(val name: String) {
	fun isLarge() = name.uppercase() == name
	fun isSmall() = name.uppercase() != name

	companion object {
		val START = Room("start")
		val END = Room("end")
	}
}

data class Connection(val from: Room, val to: Room)

data class Path(val rooms: List<Room>) {
	constructor(room: Room) : this(listOf(room))

	fun last(): Room = rooms.last()
	fun add(room: Room) = Path(rooms + room)
	fun contains(room: Room) = rooms.contains(room)
	fun containsSmallRoomTwice() = rooms
		.filter { it.isSmall() }
		.groupBy { it }
		.none { it.value.size > 1 }
}