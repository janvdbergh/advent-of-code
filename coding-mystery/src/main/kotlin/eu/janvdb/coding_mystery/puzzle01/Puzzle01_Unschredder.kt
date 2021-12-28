package eu.janvdb.coding_mystery.puzzle01

import eu.janvdb.aocutil.kotlin.readLines
import eu.janvdb.coding_mystery.PREFIX
import java.io.FileWriter
import java.io.PrintWriter
import java.util.*

fun main() {
	val unshredded = readLines(PREFIX, "puzzle01_unshredded.txt")
		.asSequence()
		.map(String::trim)
		.map(::getLinePrefix)
		.filter(Objects::nonNull)
		.toList()

	val shredded = readLines(PREFIX, "puzzle01_shredded.txt")
		.asSequence()
		.map(String::trim)
		.map { Pair(getLinePrefix(it), it) }
		.toMap()

	PrintWriter(FileWriter("output.txt")).use { writer ->
		unshredded.asSequence()
			.map(shredded::get)
			.filter(Objects::nonNull)
			.map { it!!.replace(Regex(" "), ".") }
			.forEach {
				writer.println(it)
			}
	}
}

private fun getLinePrefix(s: String): String? {
	val index = s.indexOf("  ")
	return if (index == -1) null else s.substring(0, index)
}