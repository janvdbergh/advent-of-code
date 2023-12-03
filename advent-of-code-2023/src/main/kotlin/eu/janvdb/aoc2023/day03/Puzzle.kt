package eu.janvdb.aoc2023.day03

import eu.janvdb.aocutil.kotlin.point2d.Point2D

val NUMBER_REGEX = Regex("\\d+")

class Puzzle(input: List<String>) {

	private val numbers: List<Number>
	private val symbols: List<Symbol>

	init {
		this.numbers = input.flatMapIndexed { y, line ->
			NUMBER_REGEX.findAll(line)
				.map { it.groups[0]!! }
				.map {
					Number(Point2D(it.range.first, y), it.range.last - it.range.first + 1, it.value.toInt())
				}
		}

		this.symbols = input.flatMapIndexed { y, line ->
			line.mapIndexed { x, c -> Symbol(Point2D(x, y), c) }
				.filter { it.symbol != '.' && !it.symbol.isDigit() }
		}.toList()
	}

	fun sumOfAdjacentNumbers(): Int {
		return numbers.filter { number -> symbols.any { number.isAdjacentTo(it) } }
			.sumOf { it.value }
	}

	fun getSumOfGearRatios(): Int {
		return symbols
			.filter { it.isGear() }
			.map { it.getAdjacentNumbers(numbers) }
			.filter { it.size == 2 }
			.sumOf { it[0].value * it[1].value }
	}
}

data class Number(val position: Point2D, val length: Int, val value: Int) {

	fun isAdjacentTo(symbol: Symbol): Boolean {
		val xMinOk = position.x - 1 <= symbol.position.x
		val xMaxOk = position.x + length >= symbol.position.x
		val yMinOk = position.y - 1 <= symbol.position.y
		val yMaxOk = position.y + 1 >= symbol.position.y
		return xMinOk && xMaxOk && yMinOk && yMaxOk
	}
}

data class Symbol(val position: Point2D, val symbol: Char) {
	fun isGear() = symbol == '*'
	fun getAdjacentNumbers(numbers: List<Number>) = numbers.filter { it.isAdjacentTo(this) }
}