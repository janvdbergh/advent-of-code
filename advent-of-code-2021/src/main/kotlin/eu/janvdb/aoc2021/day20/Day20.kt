package eu.janvdb.aoc2021.day20

import eu.janvdb.aocutil.kotlin.readGroupedLines
import eu.janvdb.aocutil.kotlin.runWithTimer
import java.util.*

const val FILENAME = "input20.txt"

fun main() {
	val groups = readGroupedLines(2021, FILENAME)
	val pattern = Pattern.parse(groups[0])
	val image = Image.parse(groups[1])

	runWithTimer("Part 1") {
		val result1 = image.applyPattern(pattern).applyPattern(pattern)
		result1.print()
	}

	runWithTimer("Part 2") {
		var result2 = image
		for (i in 0 until 50) {
			result2 = result2.applyPattern(pattern)
		}
		result2.print()
	}
}

class Pattern(val values: BitSet) {
	val size = values.size()

	fun print() {
		for (i in 0 until values.size()) {
			val ch = if (values[i]) '#' else '.'
			print(ch)
		}
		println()
	}

	operator fun get(patternIndex: Int) = values[patternIndex]

	companion object {
		fun parse(lines: List<String>): Pattern {
			val allData = lines.fold(StringBuilder()) { acc, it -> acc.append(it) }.toString()
			val bits = BitSet(allData.length)
			for (i in allData.indices) {
				bits[i] = allData[i] == '#'
			}
			return Pattern(bits)
		}
	}
}

class Image(val width: Int, val height: Int, val pixels: BitSet, val defaultValue: Boolean) {
	fun applyPattern(pattern: Pattern): Image {
		val newWidth = width + 2
		val newHeight = height + 2
		val newPixels = BitSet(newHeight * newWidth)

		fun newIndex(x: Int, y: Int): Int {
			return x + 1 + newWidth * (y + 1) // Indices go from -1 .. n
		}

		for (y in -1 until height + 1) {
			for (x in -1 until width + 1) {
				val patternIndex = getPatternIndex(x, y)
				newPixels[newIndex(x, y)] = pattern[patternIndex]
			}
		}

		val nextDefaultValue = if (defaultValue) pattern[pattern.size -1] else pattern[0]
		return Image(newWidth, newHeight, newPixels, nextDefaultValue)
	}

	fun print() {
		for (y in 0 until height) {
			for (x in 0 until width) {
				val ch = if (getPixel(x, y)) '#' else '.'
				print(ch)
			}
			println()
		}
		println("Pixels set: " + pixels.cardinality())
	}

	private fun getPatternIndex(x: Int, y: Int): Int {
		var result = 0
		for (y1 in y - 1..y + 1) {
			for (x1 in x - 1..x + 1) {
				result = 2 * result + if (getPixel(x1, y1)) 1 else 0
			}
		}
		return result
	}

	private fun getPixel(x: Int, y: Int): Boolean {
		if (x < 0 || y < 0 || x >= width || y >= height) return defaultValue
		return pixels[x + y * width]
	}

	companion object {
		fun parse(lines: List<String>): Image {
			val height = lines.size
			val width = lines[0].length
			val pixels = BitSet(width * height)
			for (y in 0 until height) {
				for (x in 0 until width) {
					pixels[y * width + x] = lines[y][x] == '#'
				}
			}
			return Image(width, height, pixels, false)
		}
	}
}