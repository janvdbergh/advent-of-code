package eu.janvdb.aoc2020.common

import java.io.File

fun readLines(fileName: String): List<String> {
	return File("advent-of-code-2020/inputs/${fileName}").readLines()
}

fun readGroupedLines(fileName: String): List<List<String>> {
	return groupLines(readLines(fileName))
}

private fun groupLines(lines: List<String>): List<List<String>> {
	val result = mutableListOf<List<String>>()

	var subLines = mutableListOf<String>()
	lines.forEach {
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