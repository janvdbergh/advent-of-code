package eu.janvdb.aocutil.kotlin

import eu.janvdb.aocutil.kotlin.point2d.Point2D

class Bitmap(val points: Set<Point2D>) {
	val minX: Int get() = points.minByOrNull { it.x }?.x ?: 0
	val maxX: Int get() = points.maxByOrNull { it.x }?.x ?: 0
	val minY: Int get() = points.minByOrNull { it.y }?.y ?: 0
	val maxY: Int get() = points.maxByOrNull { it.y }?.y ?: 0
	val width: Int get() = maxX - minX + 1
	val height: Int get() = maxY - minY + 1

	fun intersectsWith(other: Bitmap) = points.intersect(other.points).isNotEmpty()

	fun move(dx: Int, dy: Int) = Bitmap(points.map { Point2D(it.x + dx, it.y + dy) }.toSet())
	fun moveDown(steps: Int = 1) = move(0, -steps)
	fun moveUp(steps: Int = 1) = move(0, steps)
	fun moveRight(steps: Int = 1) = move(steps, 0)
	fun moveLeft(steps: Int = 1) = move(-steps, 0)

	fun add(bitmap: Bitmap): Bitmap {
		return Bitmap(points + bitmap.points)
	}

	override fun toString(): String {
		val minX = this.minX
		val maxX = this.maxX
		val minY = this.minY
		val maxY = this.maxY
		val builder = StringBuilder()
		for (y in maxY downTo minY) {
			for (x in minX..maxX) {
				builder.append(if (points.contains(Point2D(x, y))) '#' else '.')
			}
			builder.append('\n')
		}
		return builder.toString()
	}
}
