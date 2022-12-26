package eu.janvdb.aocutil.kotlin

open class SquareArrayMatrix<T>(val dimension: Int, val tiles: List<T>) {
	fun tile(x: Int, y: Int) = tiles[y * dimension + x]

	fun rotateLeft(): SquareArrayMatrix< T> {
		val newTiles = tiles.toMutableList()
		for (y in 0 until dimension) {
			for (x in 0 until dimension) {
				newTiles[x + y * dimension] = tile(dimension - y - 1, x)
			}
		}
		return SquareArrayMatrix(dimension, newTiles)
	}

	fun rotateRight(): SquareArrayMatrix<T> {
		val newTiles = tiles.toMutableList()
		for (y in 0 until dimension) {
			for (x in 0 until dimension) {
				newTiles[x + y * dimension] = tile(y, dimension - x - 1)
			}
		}
		return SquareArrayMatrix(dimension, newTiles)
	}

	override fun toString(): String {
		val builder = StringBuilder()
		for (y in 0 until dimension) {
			for (x in 0 until dimension) {
				builder.append(tile(x, y))
			}
			builder.append('\n')
		}
		return builder.toString()
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as SquareArrayMatrix<*>

		if (dimension != other.dimension) return false
		if (tiles != other.tiles) return false

		return true
	}

	override fun hashCode(): Int {
		var result = dimension
		result = 31 * result + tiles.hashCode()
		return result
	}
}