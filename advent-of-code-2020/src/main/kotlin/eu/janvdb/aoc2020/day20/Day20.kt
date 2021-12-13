package eu.janvdb.aoc2020.day20

import eu.janvdb.aocutil.kotlin.readGroupedLines

val MONSTER = Picture(
	listOf(
		"                  # ",
		"#    ##    ##    ###",
		" #  #  #  #  #  #   "
	)
)

fun main() {
	val tiles = readGroupedLines(2020, "input20.txt").map(::MapTile)

	val topLeftCorner = getTopLeftCorner(tiles)
	val fullPicture = buildPictureFromCornerDown(tiles, topLeftCorner)

	val occurrences = fullPicture.findImageOccurrences(MONSTER)
	val pictureWithoutMonsters = occurrences.fold(fullPicture) { picture, occurrence ->
		picture.removeImageOccurrence(occurrence.first, occurrence.second)
	}
	println(pictureWithoutMonsters.numberOfPixelsSet())
}

private fun getTopLeftCorner(tiles: List<MapTile>): MapTile {
	// Corners only attach to two other tiles.
	// This means that of their eight border checksums, four will not be found in other tiles
	val borderValues = tiles.flatMap { it.borderValues }
	val borderValuesOccurringOnce = borderValues.groupBy { it }.filterValues { it.size == 1 }.map { it.key }.toSet()
	val corners = tiles.filter { borderValuesOccurringOnce.intersect(it.borderValues).size == 4 }

	// Result of part 1: multiply id of corners
	val cornerProduct = corners.fold(1L) { temp, corner -> temp * corner.id }
	println(cornerProduct)

	// Take the first corner and orient it so that its top and left edge don't match any other
	return corners[0].getAllTransformations()
		.first { it.borderLeft in borderValuesOccurringOnce && it.borderTop in borderValuesOccurringOnce }
}

private fun buildPictureFromCornerDown(allTiles: List<MapTile>, topLeft: MapTile): Picture {
	fun findTileMatchingRight(tileToMatch: MapTile): MapTile? {
		val tile = allTiles.filter { it.id != tileToMatch.id }.singleOrNull { tileToMatch.borderRight in it.borderValues }
			?: return null
		return tile.getAllTransformations().single { it.borderLeftFlipped == tileToMatch.borderRight }
	}

	fun getTileMatchingBottom(tileToMatch: MapTile): MapTile? {
		val tile = allTiles.filter { it.id != tileToMatch.id }.singleOrNull { tileToMatch.borderBottom in it.borderValues }
			?: return null
		return tile.getAllTransformations().single { it.borderTopFlipped == tileToMatch.borderBottom }
	}


	val tilesOrdered = mutableListOf<MutableList<MapTile>>()

	var currentLeft: MapTile? = topLeft
	while (currentLeft != null) {
		val currentLine = mutableListOf(currentLeft)
		tilesOrdered.add(currentLine)

		var current = findTileMatchingRight(currentLeft)
		while (current != null) {
			currentLine.add(current)
			current = findTileMatchingRight(current)
		}

		currentLeft = getTileMatchingBottom(currentLeft)
	}

	return tilesOrdered.combine()
}