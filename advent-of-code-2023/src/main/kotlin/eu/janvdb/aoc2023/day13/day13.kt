package eu.janvdb.aoc2023.day13

import eu.janvdb.aocutil.kotlin.readGroupedLines
import kotlin.math.min

//const val FILENAME = "input13-test.txt"
const val FILENAME = "input13.txt"

fun main() {
	val matrices = readGroupedLines(2023, FILENAME).map { Matrix.parse(it) }

	val sums1 = matrices.map { it.part1() }
	println(sums1.sum())

	val sums2 = matrices.map { it.part2() }
	println(sums2.sum())
}

data class Matrix(val height: Int, val width: Int, val cells: List<Boolean>) {

	fun part1(): Int {
		return findMirrors().sum()
	}

	fun part2(): Int {
		return findMirrorsWhenSmudged().sum()
	}

	fun findMirrorsWhenSmudged(): List<Int> {
		fun replaceCell(index: Int): Matrix {
			val newCells = cells.toMutableList()
			newCells[index] = !cells[index]
			return Matrix(height, width, newCells)
		}

		val baseLine = findMirrors().toSet()

		return cells.indices.asSequence()
			.map { index -> replaceCell(index) }
			.map { it.findMirrors().minus(baseLine) }
			.filter { it.size == 1 }
			.first()
	}


	private fun findMirrors(): List<Int> = findMirrorRows().map { it * 100 } + findMirrorColumns()

	private fun findMirrorRows(): List<Int> {
		return (1..<height).filter { isMirroredAtRow(it) }
	}

	private fun findMirrorColumns(): List<Int> {
		return (1..<width).filter { isMirroredAtColumn(it) }
	}

	private fun isMirroredAtRow(row: Int): Boolean {
		val rowsAbove = row
		val rowsBelow = height - row
		for (y in 0..<min(rowsAbove, rowsBelow)) {
			for (x in 1..width) {
				if (getCell(x, row - y) != getCell(x, row + y + 1)) return false
			}
		}
		return true
	}

	private fun isMirroredAtColumn(column: Int): Boolean {
		val columnsLeft = column
		val columnsRight = width - column
		for (x in 0..<min(columnsLeft, columnsRight)) {
			for (y in 1..height) {
				if (getCell(column - x, y) != getCell(column + x + 1, y)) return false
			}
		}
		return true
	}

	private fun getCell(x: Int, y: Int): Boolean {
		// we work one-based
		return cells[(y - 1) * width + x - 1]
	}

	companion object {
		fun parse(input: List<String>): Matrix {
			val height = input.size
			val width = input[0].length
			val cells = input.flatMap { it.map { it == '#' } }
			return Matrix(height, width, cells)
		}
	}
}