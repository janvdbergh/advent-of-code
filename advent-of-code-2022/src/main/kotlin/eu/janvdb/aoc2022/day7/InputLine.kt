package eu.janvdb.aoc2022.day7

interface InputLine
data class InputLineCd(val dir: String) : InputLine
data class InputLineDir(val dir: String) : InputLine
data class InputLineFile(val file: String, val size: Long) : InputLine

fun String.toInputLine(): InputLine {
	if (this.startsWith("$ cd"))
		return InputLineCd(this.substring(5))
	if (this.startsWith("dir"))
		return InputLineDir(this.substring(4))

	val parts = this.split(" ")
	return InputLineFile(parts[1], parts[0].toLong())
}