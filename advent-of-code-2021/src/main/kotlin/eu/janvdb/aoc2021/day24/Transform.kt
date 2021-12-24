package eu.janvdb.aoc2021.day24

import eu.janvdb.aocutil.kotlin.readLines

const val FILENAME = "input24.txt"

val LINE_PATTERN = Regex("([a-z]{3}) ([a-z]) ?(.+)?")

fun main() {
	var input = 0
	val variables = mutableMapOf(
		Pair("x", 0),
		Pair("y", 0),
		Pair("z", 0),
		Pair("w", 0)
	)

	fun transform(line: String): String {
		val match = LINE_PATTERN.matchEntire(line)!!
		val (_, instruction, arg1, arg2) = match.groupValues

		val var1 = arg1 + (variables[arg1]!! + 1)
		val var2 = arg1 + variables[arg1]!!
		val var3 = if (variables.containsKey(arg2)) arg2 + variables.get(arg2) else arg2

		variables[arg1] = variables[arg1]!! + 1

		return when (instruction) {
			"inp" -> "val $var1 = i${input++}"
			"add" -> "val $var1 = $var2 + $var3"
			"mul" -> "val $var1 = $var2 * $var3"
			"div" -> "val $var1 = $var2 / $var3"
			"mod" -> "val $var1 = $var2 % $var3"
			"eql" -> "val $var1 = if ($var2 == $var3) 1 else 0"
			else -> throw IllegalArgumentException()
		}
	}

	println("val x0 = 0")
	println("val y0 = 0")
	println("val z0 = 0")
	println("val w0 = 0")
	readLines(2021, FILENAME)
		.asSequence()
		.map { line -> transform(line) }
		.forEach(::println)
	println("return z${variables["z"]}")
}


