package eu.janvdb.aoc2020.day02

import eu.janvdb.aocutil.kotlin.readLines
import kotlin.system.exitProcess

fun main() {
	val lines = readLines("input02.txt")

	// 1-13 f: ffffffffffdfzfffff
	val regexp = Regex(pattern = "^([0-9]+)-([0-9]+) ([a-z]): ([a-z]+)$")

	var ok1 = 0
	var ok2 = 0
	lines.forEach {
		val matchResult = regexp.matchEntire(it)
		if (matchResult == null) {
			println("Invalid input: $it")
			exitProcess(1)
		}

		val min = matchResult.groupValues[1].toInt()
		val max = matchResult.groupValues[2].toInt()
		val ch = matchResult.groupValues[3][0]
		val password = matchResult.groupValues[4]
		val count = password.count { c -> c == ch }

		val matches = count >= min && count <= max
		if (matches) ok1++

		val contains1 = password[min - 1] == ch
		val contains2 = password[max - 1] == ch
		if ((contains1 && !contains2) || (contains2 && !contains1)) ok2++
	}

	println(ok1)
	println(ok2)
}