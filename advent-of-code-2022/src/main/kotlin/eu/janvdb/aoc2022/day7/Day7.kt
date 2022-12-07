package eu.janvdb.aoc2022.day7

import eu.janvdb.aocutil.kotlin.readLines

//const val FILENAME = "input07-test.txt"
const val FILENAME = "input07.txt"

fun main() {
	val rootDirectory = readStructure()
	part1(rootDirectory)
	part2(rootDirectory)
}

private fun readStructure(): Directory {
	val rootDirectory = Directory("/")
	var currentDirectory = rootDirectory

	fun handleInput(inputLine: InputLine) {
		when (inputLine) {
			is InputLineCd -> currentDirectory = currentDirectory.changeDir(inputLine.dir)
			is InputLineFile -> currentDirectory.addFile(inputLine.size)
		}
	}

	readLines(2022, FILENAME)
		.filter { it != "$ ls" }
		.map(String::toInputLine)
		.forEach(::handleInput)
	return rootDirectory
}

private fun part1(rootDirectory: Directory) {
	val part1 = rootDirectory.all()
		.filter { it.size <= 100000L }
		.sumOf { it.size }
	println(part1)
}

private fun part2(rootDirectory: Directory) {
	val requiredSpace = 30000000
	val freeSpace = 70000000 - rootDirectory.size
	val spaceToFree = requiredSpace - freeSpace
	val free = rootDirectory.all()
		.filter { it.size >= spaceToFree }
		.sortedBy { it.size }
	free.forEach { println(it) }
	val toFree = free
		.first()
	println(toFree.size)
}

class Directory(val name: String, val parent: Directory? = null) {
	var size = 0L
	val directories = mutableMapOf<String, Directory>()

	fun changeDir(dir: String): Directory {
		return when (dir) {
			"/" -> parent?.changeDir(dir) ?: this
			".." -> parent ?: throw IllegalStateException()
			else -> directories.computeIfAbsent(dir) { Directory(dir, this) }
		}
	}

	fun addFile(size: Long) {
		this.size += size
		parent?.addFile(size)
	}

	fun all(): List<Directory> = directories.values.flatMap { it.all() } + this

	override fun toString(): String {
		return "Directory(name=$name, size=$size, directories=$directories)"
	}
}