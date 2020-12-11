package eu.janvdb.aoc2020.day11

enum class Seat(val symbol: Char) {
	FLOOR('.'), EMPTY('L'), FILLED('#')
}

fun toSeat(symbol: Char): Seat {
	return Seat.values()
		.find { it.symbol == symbol }!!
}

class Seating {

	private val width: Int
	private val height: Int
	private val seats: List<Seat>

	constructor(input: List<String>) {
		width = input[0].length
		height = input.size
		seats = IntRange(0, height - 1)
			.flatMap { x -> IntRange(0, width - 1).map { y -> input[x][y] } }
			.map(::toSeat)
	}

	constructor(width: Int, height: Int, seats: List<Seat>) {
		this.width = width
		this.height = height
		this.seats = seats
	}

	fun getSeat(x: Int, y: Int): Seat {
		if (x < 0 || x >= height || y < 0 || y >= width) return Seat.EMPTY
		return seats[x * width + y]
	}

	fun print() {
		for (x in 0 until height) {
			for (y in 0 until width) {
				val seat = getSeat(x, y)
				print(seat.symbol)
			}
			println()
		}
		val numberFilled = seats.count { seat -> seat == Seat.FILLED }
		println("${numberFilled} seats filled.")
		println()
	}

	fun step1(): Seating {
		fun filled(x: Int, y: Int): Int {
			return if (getSeat(x, y) == Seat.FILLED) 1 else 0
		}

		fun calculateNext(x: Int, y: Int): Seat {
			val filledNeighbours =
				filled(x - 1, y - 1) + filled(x - 1, y) + filled(x - 1, y + 1) +
						filled(x, y - 1) + filled(x, y + 1) +
						filled(x + 1, y - 1) + filled(x + 1, y) + filled(x + 1, y + 1)

			val seat = getSeat(x, y)
			if (seat == Seat.FILLED && filledNeighbours >= 4) return Seat.EMPTY
			if (seat == Seat.EMPTY && filledNeighbours == 0) return Seat.FILLED
			return seat
		}

		val newSeats = IntRange(0, height - 1)
			.flatMap { x -> IntRange(0, width - 1).map { y -> calculateNext(x, y) } }

		return Seating(width, height, newSeats)
	}

	fun step2(): Seating {
		fun filled(x: Int, y: Int, stepX: Int, stepY: Int): Int {
			when (getSeat(x, y)) {
				Seat.FILLED -> return 1
				Seat.EMPTY -> return 0
				Seat.FLOOR -> return filled(x + stepX, y + stepY, stepX, stepY)
			}
		}

		fun calculateNext(x: Int, y: Int): Seat {
			val filledNeighbours = filled(x - 1, y - 1, -1, -1) +
					filled(x - 1, y, -1, 0) +
					filled(x - 1, y + 1, -1, +1) +
					filled(x, y - 1, 0, -1) +
					filled(x, y + 1, 0, +1) +
					filled(x + 1, y - 1, +1, -1) +
					filled(x + 1, y, +1, 0) +
					filled(x + 1, y + 1, +1, +1)

			val seat = getSeat(x, y)
			if (seat == Seat.FILLED && filledNeighbours >= 5) return Seat.EMPTY
			if (seat == Seat.EMPTY && filledNeighbours == 0) return Seat.FILLED
			return seat
		}

		val newSeats = IntRange(0, height - 1)
			.flatMap { x -> IntRange(0, width - 1).map { y -> calculateNext(x, y) } }

		return Seating(width, height, newSeats)
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as Seating

		if (width != other.width) return false
		if (height != other.height) return false
		if (seats != other.seats) return false

		return true
	}

	override fun hashCode(): Int {
		var result = width
		result = 31 * result + height
		result = 31 * result + seats.hashCode()
		return result
	}


}