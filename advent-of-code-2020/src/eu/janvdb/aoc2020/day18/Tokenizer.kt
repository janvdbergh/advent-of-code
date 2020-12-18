package eu.janvdb.aoc2020.day18

import java.math.BigInteger

object Tokenizer {
	fun tokenize(input: String): List<Token> {
		val expressionWithoutSpaces = input.replace(" ", "")
		return tokenizeInternal(expressionWithoutSpaces)
	}

	private fun tokenizeInternal(input: String): List<Token> {
		var index = 0
		val tokens = mutableListOf<Token>()

		fun findClosingParenthesis(): Int {
			var depth = 0
			for (i in index until input.length) {
				when (input[i]) {
					'(' -> depth++
					')' -> depth--
				}
				if (depth == 0) return i
			}
			throw IllegalArgumentException(input)
		}

		while (index < input.length) {
			when (input[index]) {
				'+' -> {
					tokens += PlusToken()
					index++
				}
				'*' -> {
					tokens += MultiplyToken()
					index++
				}
				in '0'..'9' -> {
					tokens += NumberToken(BigInteger.valueOf(input.substring(index, index + 1).toLong()))
					index++
				}
				'(' -> {
					val end = findClosingParenthesis()
					val substring = input.substring(index + 1, end)
					tokens += ComplexToken(tokenize(substring))
					index = end + 1
				}
				else -> throw IllegalArgumentException(input)
			}
		}

		return tokens
	}
}

interface Token
class PlusToken : Token
class MultiplyToken : Token
data class NumberToken(val value: BigInteger) : Token
data class ComplexToken(val nestedTokens: List<Token>) : Token
