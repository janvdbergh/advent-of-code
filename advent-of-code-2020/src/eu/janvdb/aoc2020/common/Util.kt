package eu.janvdb.aoc2020.common

import java.io.File

fun readLines(fileName: String): List<String> {
	return File("advent-of-code-2020/inputs/${fileName}").readLines()
}

fun List<String>.groupLines(): List<List<String>> {
	val result = mutableListOf<List<String>>()

	var subLines = mutableListOf<String>()
	this.forEach {
		if (it.isBlank() && subLines.isNotEmpty()) {
			result.add(subLines)
			subLines = mutableListOf()
		} else {
			subLines.add(it)
		}
	}
	if (subLines.isNotEmpty()) {
		result.add(subLines)
	}
	return result
}
