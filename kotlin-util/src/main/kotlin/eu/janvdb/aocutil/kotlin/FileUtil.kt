package eu.janvdb.aocutil.kotlin

import java.io.File

fun readLines(year: Int, fileName: String): List<String> {
	return File("advent-of-code-$year/inputs/${fileName}").readLines()
}

fun readGroupedLines(fileName: String): List<List<String>> {
	return groupLines(readLines(2020, fileName))
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