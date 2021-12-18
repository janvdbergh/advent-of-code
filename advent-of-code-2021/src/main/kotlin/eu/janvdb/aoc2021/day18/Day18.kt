package eu.janvdb.aoc2021.day18

import eu.janvdb.aoc2021.day18.ExplodeResult.Companion.explosion
import eu.janvdb.aoc2021.day18.ExplodeResult.Companion.noExplosion
import eu.janvdb.aocutil.kotlin.readLines

const val FILENAME = "input18.txt"

fun main() {
	val numbers = readLines(2021, FILENAME).map(Number::parse)
	part1(numbers)
	part2(numbers)
}

private fun part1(numbers: List<Number>) {
	val result = numbers.reduce { a, b -> a + b }
	println(result)
	println(result.magnitude())
}

private fun part2(numbers: List<Number>) {
	val result = numbers.asSequence()
		.flatMap { number1 -> numbers.map { number2 -> Pair(number1, number2) } }
		.map { it.first + it.second }
		.maxOf { it.magnitude() }
	println(result)
}

abstract class Number {
	operator fun plus(other: Number): Number {
		val result = NumberElement(this, other)
		return result.simplify()
	}

	fun simplify(): Number {
		val result = explode(0).number
		if (result != this) return result.simplify()

		val result2 = split()
		if (result2 != this) return result2.simplify()
		return result2
	}


	abstract fun explode(level: Int): ExplodeResult
	abstract fun split(): Number
	abstract fun distributeLeft(value: Int): Number
	abstract fun distributeRight(value: Int): Number

	abstract fun magnitude(): Int

	companion object {
		fun parse(s: String) = readNumber(TokenStream(s))

		private fun readNumber(stream: TokenStream): Number {
			val token = stream.nextToken()
			if (token == "[") {
				val left = readNumber(stream)
				stream.nextToken() // ","
				val right = readNumber(stream)
				stream.nextToken() // "]"
				return NumberElement(left, right)
			}

			return NumberLeaf(token.toInt())
		}
	}
}

class NumberLeaf(val value: Int) : Number() {
	override fun explode(level: Int) = noExplosion(this)

	override fun split(): Number {
		return if (value > 9) NumberElement(NumberLeaf(value / 2), NumberLeaf((value + 1) / 2)) else this
	}

	override fun distributeLeft(value: Int) = NumberLeaf(this.value + value)
	override fun distributeRight(value: Int) = NumberLeaf(this.value + value)
	override fun magnitude() = value

	override fun toString() = value.toString()
}

class NumberElement(val left: Number, val right: Number) : Number() {
	override fun explode(level: Int): ExplodeResult {
		// Check left
		val resultLeft = left.explode(level + 1)
		if (resultLeft.exploded) {
			val newRight =
				if (resultLeft.distributeRight != null) right.distributeLeft(resultLeft.distributeRight) else right
			return explosion(NumberElement(resultLeft.number, newRight), resultLeft.distributeLeft, null)
		}

		// Check right
		val resultRight = right.explode(level + 1)
		if (resultRight.exploded) {
			val newLeft =
				if (resultRight.distributeLeft != null) left.distributeRight(resultRight.distributeLeft) else left
			return explosion(NumberElement(newLeft, resultRight.number), null, resultRight.distributeRight)
		}

		// Only explode if level too high and numbers left and right
		if (level > 3 && left is NumberLeaf && right is NumberLeaf) {
			return explosion(NumberLeaf(0), left.value, right.value)
		}

		// No explosion
		return noExplosion(this)
	}

	override fun split(): Number {
		val splitLeft = left.split()
		if (splitLeft != left) return NumberElement(splitLeft, right)
		val splitRight = right.split()
		if (splitRight != right) return NumberElement(left, splitRight)
		return this
	}

	override fun distributeLeft(value: Int) = NumberElement(left.distributeLeft(value), right)
	override fun distributeRight(value: Int) = NumberElement(left, right.distributeRight(value))

	override fun magnitude() = 3 * left.magnitude() + 2 * right.magnitude()

	override fun toString() = "[$left,$right]"
}

data class ExplodeResult(
	val number: Number,
	val distributeLeft: Int?,
	val distributeRight: Int?,
	val exploded: Boolean
) {
	companion object {
		fun noExplosion(number: Number) = ExplodeResult(number, null, null, false)
		fun explosion(number: Number, distributeLeft: Int?, distributeRight: Int?) = ExplodeResult(
			number, distributeLeft, distributeRight, true
		)
	}
}

class TokenStream(private val s: String) {
	var index = 0
	fun nextToken(): String {
		val result: String = if (s[index] == '[' || s[index] == ']' || s[index] == ',') {
			s.substring(index, index + 1)
		} else {
			var endIndex = index
			while (s[endIndex].isDigit()) endIndex++
			s.substring(index, endIndex)
		}

		index += result.length
		return result
	}
}