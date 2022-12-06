package eu.janvdb.aocutil.kotlin

import java.io.File

fun readLines(year: Int, fileName: String) = readLines("advent-of-code-$year", fileName)

fun readLines(prefix: String, fileName: String): List<String> {
	return File("$prefix/inputs/${fileName}").readLines()
}

fun readFile(year: Int, fileName: String) = readFile("advent-of-code-$year", fileName)

fun readFile(prefix: String, fileName: String): String {
	return File("$prefix/inputs/${fileName}").readText()
}

fun readCommaSeparatedNumbers(year: Int, fileName: String): List<Int> {
	return readLines(year, fileName)
		.flatMap { it.split(",") }
		.map { it.toInt() }
}

fun readNonSeparatedDigits(year: Int, fileName: String): List<List<Int>> {
	return readLines(year, fileName)
		.map { line -> line.toCharArray().map { ch -> ch - '0' }.toList() }
}

fun readGroupedLines(year: Int, fileName: String): List<List<String>> {
	return groupLines(readLines(year, fileName))
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