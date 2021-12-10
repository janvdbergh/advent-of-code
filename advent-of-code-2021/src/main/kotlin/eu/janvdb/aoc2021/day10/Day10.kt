package eu.janvdb.aoc2021.day10

import eu.janvdb.aocutil.kotlin.readLines
import java.util.*

const val FILENAME = "input10.txt"

val MATCHING_CHARS = mapOf(Pair('(', ')'), Pair('{', '}'), Pair('[', ']'), Pair('<', '>'))
val SCORES1 = mapOf(Pair(')', 3L), Pair(']', 57L), Pair('}', 1197L), Pair('>', 25137L))
val SCORES2 = mapOf(Pair(')', 1L), Pair(']', 2L), Pair('}', 3L), Pair('>', 4L))

fun main() {
	val scores = readLines(2021, FILENAME).map(::getSyntaxScoreForPattern)

	val result1 = scores.filter { !it.first }.sumOf { it.second }
	println(result1)

	val scores2 = scores.filter { it.first }.map { it.second }.sorted()
	val result2 = scores2[scores2.size/2]
	println(result2)
}

// First element indicates if correct, second element the score
fun getSyntaxScoreForPattern(line: String): Pair<Boolean, Long> {
	val openingCharactersFound = LinkedList<Char>()

	for(ch in line.toCharArray()) {
		when(ch) {
			'(', '{', '[', '<' -> openingCharactersFound.addLast(MATCHING_CHARS[ch]!!)
			')', '}', ']', '>' -> {
				val matchingChar = openingCharactersFound.removeLast()
				if (ch != matchingChar) {
					return Pair(false, SCORES1[ch]!!)
				}
			}
		}
	}

	val score = openingCharactersFound.foldRight(0L) { ch, acc -> acc * 5 + SCORES2[ch]!! }
	return Pair(true, score)
}