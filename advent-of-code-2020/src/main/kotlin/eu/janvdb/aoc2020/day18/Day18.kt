package eu.janvdb.aoc2020.day18

import eu.janvdb.aocutil.kotlin.readLines
import java.math.BigInteger

fun main() {
	val tokenizedLines = readLines(2020, "input18.txt").map(Tokenizer::tokenize)

	fun executePart(parser: (List<Token>) -> Expression): BigInteger {
		return tokenizedLines.asSequence()
			.map(parser)
			.map(Expression::getValue)
			.fold(BigInteger.ZERO, { x, y -> x + y })
	}

	println(executePart(Parsers::parse1))
	println(executePart(Parsers::parse2))
}




