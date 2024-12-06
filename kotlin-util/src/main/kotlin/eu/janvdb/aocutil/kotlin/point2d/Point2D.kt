package eu.janvdb.aocutil.kotlin.point2d

abstract class AbstractPoint2D<X : AbstractPoint2D<X, T>, T : Number>(val x: T, val y: T) :
	Comparable<AbstractPoint2D<X, T>> {

	protected abstract fun zero(): T
	protected abstract fun one(): T
	protected abstract fun instantiate(x: T, y: T): X
	protected abstract fun abs(x: T): T
	protected abstract fun minus(x: T): T
	protected abstract fun plus(x: T, y: T): T
	protected abstract fun minus(x: T, y: T): T
	protected abstract fun multiply(x: T, y: T): T
	protected abstract fun modulo(x: T, y: T): T
	protected abstract fun lt(x: T, y: T): Boolean
	protected abstract fun compareTo(x: T, y: T): Int

	fun manhattanDistance(): T {
		return plus(abs(x), abs(y))
	}

	fun manhattanDistanceTo(other: AbstractPoint2D<X, T>): T {
		return plus(abs(minus(x, other.x)), abs(minus(y, other.y)))
	}

	fun move(direction: Direction, amount: T = one()) = when (direction) {
		Direction.N -> instantiate(x, minus(y, amount))
		Direction.E -> instantiate(plus(x, amount), y)
		Direction.S -> instantiate(x, plus(y, amount))
		Direction.W -> instantiate(minus(x, amount), y)
	}

	fun moveWithingBounds(direction: Direction, amount: T, maxX: T, maxY: T): AbstractPoint2D<X, T> {
		fun constrainTo(value: T, max: T): T {
			val result = modulo(value, max)
			return if (lt(result, zero())) plus(result, max) else result
		}

		return when (direction) {
			Direction.N -> instantiate(x, constrainTo(minus(y, amount), maxY))
			Direction.E -> instantiate(constrainTo(plus(x, amount), maxX), y)
			Direction.S -> instantiate(x, constrainTo(plus(y, amount), maxY))
			Direction.W -> instantiate(constrainTo(minus(x, amount), maxX), y)
		}
	}

	fun move(direction: AbstractPoint2D<X, T>, amount: T = one()): AbstractPoint2D<X, T> {
		return instantiate(multiply(plus(x, amount), direction.x), multiply(plus(y, amount), direction.y))
	}

	fun left(amount: T = one()) = instantiate(minus(x, amount), y)
	fun right(amount: T = one()) = instantiate(plus(x, amount), y)
	fun up(amount: T = one()) = instantiate(x, minus(y, amount))
	fun down(amount: T = one()) = instantiate(x, plus(y, amount))

	fun rotateLeft(amount: Int): AbstractPoint2D<X, T> {
		var actualAmount = amount % 360
		if (actualAmount < 0) actualAmount += 360
		return when (actualAmount) {
			0 -> this
			90 -> instantiate(minus(y), x)
			180 -> instantiate(minus(x), minus(y))
			270 -> instantiate(y, minus(x))
			else -> throw IllegalArgumentException(amount.toString())
		}
	}

	fun rotateRight(amount: Int): AbstractPoint2D<X, T> {
		return rotateLeft(360 - amount)
	}

	override fun compareTo(other: AbstractPoint2D<X, T>): Int {
		val dy = compareTo(y, other.y)
		return if (dy != 0) dy else compareTo(x, other.x)
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as AbstractPoint2D<*, *>
		return (x == other.x) && (y == other.y)
	}

	override fun hashCode(): Int {
		var result = x.hashCode()
		result = 31 * result + y.hashCode()
		return result
	}

	override fun toString(): String {
		return "($x, $y)"
	}
}

class Point2D(x: Int, y: Int) : AbstractPoint2D<Point2D, Int>(x, y) {
	override fun zero() = 0

	override fun one() = 1


	override fun compareTo(x: Int, y: Int) = x.compareTo(y)

	override fun lt(x: Int, y: Int) = x < y

	override fun modulo(x: Int, y: Int) = x % y

	override fun multiply(x: Int, y: Int) = x * y

	override fun minus(x: Int, y: Int) = x - y

	override fun plus(x: Int, y: Int) = x + y

	override fun minus(x: Int) = -x

	override fun abs(x: Int) = kotlin.math.abs(x)

	override fun instantiate(x: Int, y: Int) = Point2D(x, y)

	companion object {
		fun createCommaSeparated(s: String): Point2D {
			val pair = s.split(",")
			return Point2D(pair[0].trim().toInt(), pair[1].trim().toInt())
		}
	}
}

class Point2DLong(x: Long, y: Long) : AbstractPoint2D<Point2DLong, Long>(x, y) {
	constructor(x: Int, y: Int) : this(x.toLong(), y.toLong())

	override fun zero() = 0L

	override fun one() = 1L


	override fun compareTo(x: Long, y: Long) = x.compareTo(y)

	override fun lt(x: Long, y: Long) = x < y

	override fun modulo(x: Long, y: Long) = x % y

	override fun multiply(x: Long, y: Long) = x * y

	override fun minus(x: Long, y: Long) = x - y

	override fun plus(x: Long, y: Long) = x + y

	override fun minus(x: Long) = -x

	override fun abs(x: Long) = kotlin.math.abs(x)

	override fun instantiate(x: Long, y: Long) = Point2DLong(x, y)

	companion object {
		fun createCommaSeparated(s: String): Point2D {
			val pair = s.split(",")
			return Point2D(pair[0].trim().toInt(), pair[1].trim().toInt())
		}
	}
}


