package eu.janvdb.aoc2020.day20

import eu.janvdb.aoc2020.common.Coordinate

open class Picture(val width: Int, val height: Int, val pixels: List<Boolean>) {

	constructor(width: Int, height: Int, hasPixel: (Int, Int) -> Boolean) : this(
		width = width, height = height,
		pixels = List(width * height) { hasPixel.invoke(it % width, it / width) }
	)

	constructor(lines: List<String>) : this(
		width = lines.maxOf { it.length },
		height = lines.size,
		pixels = lines.joinToString(separator = "").map { it == '#' }
	)

	fun get(x: Int, y: Int): Boolean {
		if (x < 0 || x >= width || y < 0 || y >= height) throw IllegalArgumentException()
		return pixels[x + y * width]
	}

	fun getSafe(x: Int, y: Int): Boolean {
		if (x < 0 || x >= width || y < 0 || y >= height) return false
		return pixels[x + y * width]
	}

	fun numberOfPixelsSet() = pixels.count { it }

	fun flipHorizontal(): Picture = Picture(width, height) { x, y -> get(width - 1 - x, y) }
	fun rotateRight(): Picture = Picture(height, width) { x, y -> get(y, height - 1 - x) }
	fun rotateLeft(): Picture = Picture(height, width) { x, y -> get(width - 1 - y, x) }

	fun getAllTransformations(): List<Picture> {
		val rotated1 = this.rotateLeft()
		val rotated2 = rotated1.rotateLeft()
		val rotated3 = this.rotateRight()
		return listOf(
			this, this.flipHorizontal(),
			rotated1, rotated1.flipHorizontal(),
			rotated2, rotated2.flipHorizontal(),
			rotated3, rotated3.flipHorizontal(),
		)
	}

	fun findImageOccurrences(image: Picture): List<Pair<Picture, Coordinate>> {
		fun findImage(transformedImage: Picture): MutableList<Pair<Picture, Coordinate>> {
			fun hasImageAt(x: Int, y: Int): Boolean {
				for (y1 in 0 until transformedImage.height) {
					for (x1 in 0 until transformedImage.width) {
						val imagePixel = transformedImage.get(x1, y1)
						val thisPixel = get(x + x1, y + y1)
						if (imagePixel && !thisPixel) return false
					}
				}
				return true
			}

			val result = mutableListOf<Pair<Picture, Coordinate>>()
			for (y in 0 until height - transformedImage.height + 1) {
				for (x in 0 until width - transformedImage.width + 1) {
					if (hasImageAt(x, y)) {
						result.add(Pair(transformedImage, Coordinate(x, y)))
					}
				}
			}
			return result
		}

		return image.getAllTransformations().flatMap(::findImage)
	}

	fun removeImageOccurrence(image: Picture, coordinate: Coordinate): Picture {
		return Picture(width = width, height = height) { x, y ->
			get(x, y) && !image.getSafe(x - coordinate.x, y - coordinate.y)
		}
	}

	override fun toString(): String = buildString {
		for (y in 0 until height) {
			for (x in 0 until width) {
				val ch = if (get(x, y)) "#" else "."
				append(ch)
			}
			append('\n')
		}
	}
}