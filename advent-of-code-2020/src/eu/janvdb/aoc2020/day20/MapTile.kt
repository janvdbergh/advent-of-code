package eu.janvdb.aoc2020.day20

import eu.janvdb.aoc2020.day20.MapTile.Companion.SIZE

class MapTile(val id: Long, val picture: Picture) {

	constructor(lines: List<String>) : this(
		id = NUMBER_REGEX.matchEntire(lines[0])!!.groupValues[1].toLong(),
		picture = Picture(lines.subList(1, SIZE + 1))
	)

	val borderTop = getBorder { i -> picture.get(i, 0) }
	val borderRight = getBorder { i -> picture.get(SIZE - 1, i) }
	val borderBottom = getBorder { i -> picture.get(SIZE - 1 - i, SIZE - 1) }
	val borderLeft = getBorder { i -> picture.get(0, SIZE - 1 - i) }
	val borderTopFlipped = getBorder { i -> picture.get(SIZE - 1 - i, 0) }
	val borderRightFlipped = getBorder { i -> picture.get(SIZE - 1, SIZE - 1 - i) }
	val borderBottomFlipped = getBorder { i -> picture.get(i, SIZE - 1) }
	val borderLeftFlipped = getBorder { i -> picture.get(0, i) }

	val borderValues = sortedSetOf(
		borderTop, borderTopFlipped, borderRight, borderRightFlipped,
		borderBottom, borderBottomFlipped, borderLeft, borderLeftFlipped
	)

	private fun getBorder(getter: (Int) -> (Boolean)): Int {
		var result = 0
		for (i in 0 until SIZE) {
			result *= 2
			if (getter.invoke(i)) result++
		}
		return result
	}

	fun getAllTransformations(): List<MapTile> = picture.getAllTransformations().map { MapTile(id, it)}

	fun get(x: Int, y: Int): Boolean = picture.get(x, y)

	override fun toString(): String = buildString {
		append("Id: $id\n")
		append(picture.toString())
	}

	companion object {
		const val SIZE = 10
		private val NUMBER_REGEX = Regex("Tile (\\d+):")
	}
}

fun List<List<MapTile>>.combine(): Picture = Picture(
	width = getCombinedWidth(this),
	height = getCombinedHeight(this),
	pixels = combineTilePixels(this)
)

private fun combineTilePixels(tiles: List<List<MapTile>>): List<Boolean> {
	val width = getCombinedWidth(tiles)
	val height = getCombinedHeight(tiles)
	return List(width * height) {
		val x = it % width
		val y = it / width
		tiles[y / 8][x / 8].get(x % 8 + 1, y % 8 + 1)
	}
}

private fun getCombinedHeight(tiles: List<List<MapTile>>) = tiles.size * (SIZE - 2)
private fun getCombinedWidth(tiles: List<List<MapTile>>) = tiles[0].size * (SIZE - 2)

