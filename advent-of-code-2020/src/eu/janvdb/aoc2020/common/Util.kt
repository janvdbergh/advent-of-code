package eu.janvdb.aoc2020.common

import java.io.File

fun readLines(fileName: String): List<String> {
	return File("advent-of-code-2020/inputs/${fileName}").readLines()
}

fun <T> groupLines(lines: List<String>, transform: (List<String>) -> T): List<T> {
	val result = mutableListOf<T>()

	var subLines = mutableListOf<String>()
	lines.forEach {
		if (it.isBlank() && subLines.isNotEmpty()) {
			result.add(transform(subLines))
			subLines = mutableListOf()
		} else {
			subLines.add(it)
		}
	}

	if (subLines.isNotEmpty()) {
		result.add(transform(subLines))
	}

	return result
}
