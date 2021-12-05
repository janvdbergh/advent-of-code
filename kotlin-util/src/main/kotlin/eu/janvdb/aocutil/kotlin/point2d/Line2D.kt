package eu.janvdb.aocutil.kotlin.point2d

import kotlin.math.max
import kotlin.math.min

data class Line2D(val a: Point2D, val b: Point2D) {
	fun getPointsHorizontallyVerticallyOrDiagonally(): Sequence<Point2D> {
		if (a.x == b.x) {
			return IntRange(min(a.y, b.y), max(a.y, b.y)).asSequence().map { y -> Point2D(a.x, y) }
		}

		val direction = (a.y - b.y) / (a.x - b.x)
		val offset = a.y - direction * a.x
		return IntRange(min(a.x, b.x), max(a.x, b.x)).asSequence().map { x -> Point2D(x, x * direction + offset) }
	}
}