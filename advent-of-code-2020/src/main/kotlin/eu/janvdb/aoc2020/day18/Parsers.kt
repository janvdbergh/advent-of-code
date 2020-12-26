package eu.janvdb.aoc2020.day18

import java.math.BigInteger

object Parsers {

	fun parse1(tokens: List<Token>): Expression {
		if (tokens.isEmpty()) throw IllegalArgumentException("Empty tokens")

		val lastExpression = when (val lastToken = tokens.last()) {
			is ComplexToken -> parse1(lastToken.nestedTokens)
			is NumberToken -> ValueExpression(lastToken.value)
			else -> throw IllegalArgumentException("Expected right hand expression")
		}
		if (tokens.size == 1) return lastExpression

		val firstExpression = parse1(tokens.subList(0, tokens.size - 2))

		return when (tokens[tokens.size - 2]) {
			is PlusToken -> OperatorExpression(Operator.PLUS, lastExpression, firstExpression)
			is MultiplyToken -> OperatorExpression(Operator.MULTIPLY, lastExpression, firstExpression)
			else -> throw IllegalArgumentException("Invalid operator")
		}
	}

	fun parse2(tokens: List<Token>): Expression {
		if (tokens.isEmpty()) throw IllegalArgumentException("Empty tokens")

		if (tokens.size == 1) {
			return when (val lastToken = tokens.last()) {
				is ComplexToken -> parse2(lastToken.nestedTokens)
				is NumberToken -> ValueExpression(lastToken.value)
				else -> throw IllegalArgumentException("Expected right hand expression")
			}
		}

		val multiplyIndex = tokens.indexOfLast { it is MultiplyToken }
		if (multiplyIndex != -1) return OperatorExpression(
			Operator.MULTIPLY,
			parse2(tokens.subList(0, multiplyIndex)),
			parse2(tokens.subList(multiplyIndex + 1, tokens.size))
		)

		val plusIndex = tokens.indexOfLast { it is PlusToken }
		if (plusIndex != -1) return OperatorExpression(
			Operator.PLUS,
			parse2(tokens.subList(0, plusIndex)),
			parse2(tokens.subList(plusIndex + 1, tokens.size))
		)

		throw IllegalArgumentException("No operator found")
	}
}

interface Expression {
	fun getValue(): BigInteger
}

class ValueExpression(private val x: BigInteger) : Expression {
	override fun getValue(): BigInteger = x
	override fun toString(): String = x.toString()
}

class OperatorExpression(private val o: Operator, private val x: Expression, private val y: Expression) : Expression {
	override fun getValue(): BigInteger = o.operation.invoke(x.getValue(), y.getValue())
	override fun toString(): String = "($x ${o.operator} $y)"
}

enum class Operator(val operator: Char, val operation: (BigInteger, BigInteger) -> (BigInteger)) {
	PLUS('+', { a, b -> a + b }),
	MULTIPLY('*', { a, b -> a * b }),
}