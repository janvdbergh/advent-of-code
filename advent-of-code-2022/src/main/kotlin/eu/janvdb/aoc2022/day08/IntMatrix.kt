package eu.janvdb.aoc2022.day08

open class IntMatrix(private val height: Int, private val width: Int, private val data: List<Int>) {

	fun get(x: Int, y: Int): Int = data[x + y * width]

	fun countVisibile(): Int {
		var result = 0
		for (y in 0 until height) {
			for (x in 0 until width) {
				if (isVisible(x, y)) result++
			}
		}
		return result
	}

	private fun isVisible(x: Int, y: Int): Boolean {
		val value = get(x, y)

		var visibleX1 = true
		for (x1 in 0 until x) {
			visibleX1 = visibleX1 && get(x1, y) < value
		}

		var visibleX2 = true
		for (x1 in x + 1 until width) {
			visibleX2 = visibleX2 && get(x1, y) < value
		}

		var visibleY1 = true
		for (y1 in 0 until y) {
			visibleY1 = visibleY1 && get(x, y1) < value
		}

		var visibleY2 = true
		for (y2 in y + 1 until height) {
			visibleY2 = visibleY2 && get(x, y2) < value
		}

		return visibleX1 || visibleX2 || visibleY1 || visibleY2
	}

	fun maxScenicScore(): Int {
		var result = -1
		for (y in 0 until height) {
			for (x in 0 until width) {
				val value = scenicScore(x, y)
				if (value > result) result = value
			}
		}
		return result
	}

	private fun scenicScore(x: Int, y: Int): Int {
		if (x <= 0 || y <= 0 || x >= width - 1 || y >= height - 1) return 0

		val value = get(x, y)

		var x1 = x - 1
		while (x1 > 0 && get(x1, y) < value) x1--

		var x2 = x + 1
		while (x2 < width - 1 && get(x2, y) < value) x2++

		var y1 = y - 1
		while (y1 > 0 && get(x, y1) < value) y1--

		var y2 = y + 1
		while (y2 < height - 1 && get(x, y2) < value) y2++

		return (x - x1) * (x2 - x) * (y - y1) * (y2 - y)
	}

	override fun toString(): String {
		val result = StringBuilder()
		for (y in 0 until height) {
			for (x in 0 until width) {
				result.append(get(x, y))
			}
			result.append("\n")
		}
		return result.toString()
	}

	companion object {
		fun parseFromDigits(input: List<String>): IntMatrix {
			val height = input.size
			val width = input[0].length
			val numbers = List(height * width) { index -> input[index / width][index % width].code - '0'.code }
			return IntMatrix(height, width, numbers)
		}
	}
}