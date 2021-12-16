package eu.janvdb.aoc2021.day16

import eu.janvdb.aocutil.kotlin.readLines

const val FILENAME = "input16.txt"

const val TYPE_SUM = 0
const val TYPE_PRODUCT = 1
const val TYPE_MINIMUM = 2
const val TYPE_MAXIMUM = 3
const val TYPE_LITERAL = 4
const val TYPE_GREATER_THAN = 5
const val TYPE_LESS_THAN = 6
const val TYPE_EQUALS = 7

fun main() {
	val data = readLines(2021, FILENAME).asSequence()
		.map(BitStream::fromHexadecimalString)
		.map(::readPacket)
		.toList()

	data.map(Packet::versionSum).forEach(::println)
	println()

	data.map(Packet::calculateValue).forEach(::println)
}

fun readPacket(bits: BitStream): Packet {
	val version = bits.readNumber(3)
	val typeId = bits.readNumber(3)

	if (typeId == TYPE_LITERAL) {
		val value = bits.readLargeNumber()
		return LiteralPacket(version, typeId, value)
	}

	val indicator = bits.readBoolean()
	return if (indicator) {
		val numberOfSubpackets = bits.readNumber(11)
		val subPackets = IntRange(1, numberOfSubpackets).map { readPacket(bits) }
		createOperatorPacket(version, typeId, subPackets)
	} else {
		val numberOfBitsForSubPackets = bits.readNumber(15)
		val subStream = bits.subStream(numberOfBitsForSubPackets)
		val subPackets = mutableListOf<Packet>()
		while (!subStream.isComplete()) {
			subPackets.add(readPacket(subStream))
		}
		createOperatorPacket(version, typeId, subPackets)
	}
}

private fun createOperatorPacket(version: Int, typeId: Int, subPackets: List<Packet>): OperatorPacket {
	return when (typeId) {
		TYPE_SUM -> SumPacket(version, typeId, subPackets)
		TYPE_PRODUCT -> ProductPacket(version, typeId, subPackets)
		TYPE_MINIMUM -> MinimumPacket(version, typeId, subPackets)
		TYPE_MAXIMUM -> MaximumPacket(version, typeId, subPackets)
		TYPE_GREATER_THAN -> GreaterThanPacket(version, typeId, subPackets)
		TYPE_LESS_THAN -> LessThanPacket(version, typeId, subPackets)
		TYPE_EQUALS -> EqualToPacket(version, typeId, subPackets)
		else -> throw IllegalArgumentException(typeId.toString())
	}
}

class BitStream(private val bits: BooleanArray, private var offset: Int, private val endIndex: Int) {

	fun readBoolean(): Boolean {
		if (isComplete()) throw ArrayIndexOutOfBoundsException()
		return bits[offset++]
	}

	fun readNumber(size: Int): Int {
		var result = 0
		for (i in 0 until size) {
			result = result * 2 + if (readBoolean()) 1 else 0
		}
		return result
	}

	fun readLargeNumber(): Long {
		var result = 0L
		while (true) {
			val isNotFinal = readBoolean()
			val nextDigits = readNumber(4)
			result = result * 16 + nextDigits

			if (!isNotFinal) return result
		}
	}

	fun subStream(size: Int): BitStream {
		val result = BitStream(bits, offset, offset + size)
		offset += size
		return result
	}

	fun isComplete(): Boolean {
		return offset == endIndex
	}

	companion object {
		fun fromHexadecimalString(data: String): BitStream {
			val result = BooleanArray(data.length * 4)
			for (i in data.indices) {
				val value = Integer.parseInt(data.substring(i, i + 1), 16)
				result[4 * i + 0] = value >= 8
				result[4 * i + 1] = (value % 8) >= 4
				result[4 * i + 2] = (value % 4) >= 2
				result[4 * i + 3] = (value % 2) >= 1
			}
			return BitStream(result, 0, result.size)
		}
	}
}

abstract class Packet(val version: Int, val typeId: Int) {
	abstract fun versionSum(): Int
	abstract fun calculateValue(): Long
}

class LiteralPacket(version: Int, typeId: Int, val value: Long) : Packet(version, typeId) {
	override fun versionSum(): Int = version
	override fun calculateValue() = value
}

abstract class OperatorPacket(version: Int, typeId: Int, val children: List<Packet>) : Packet(version, typeId) {
	override fun versionSum(): Int = version + children.map(Packet::versionSum).sum()
	override fun calculateValue() = combineValues(children.map(Packet::calculateValue))
	abstract fun combineValues(values: List<Long>): Long
}

class SumPacket(version: Int, typeId: Int, children: List<Packet>) : OperatorPacket(version, typeId, children) {
	override fun combineValues(values: List<Long>) = values.sum()
}

class ProductPacket(version: Int, typeId: Int, children: List<Packet>) : OperatorPacket(version, typeId, children) {
	override fun combineValues(values: List<Long>) = values.fold(1L) { acc, it -> acc * it }
}

class MinimumPacket(version: Int, typeId: Int, children: List<Packet>) : OperatorPacket(version, typeId, children) {
	override fun combineValues(values: List<Long>) = values.minOrNull()!!
}

class MaximumPacket(version: Int, typeId: Int, children: List<Packet>) : OperatorPacket(version, typeId, children) {
	override fun combineValues(values: List<Long>) = values.maxOrNull()!!
}

class GreaterThanPacket(version: Int, typeId: Int, children: List<Packet>) : OperatorPacket(version, typeId, children) {
	override fun combineValues(values: List<Long>) = if (values[0] > values[1]) 1L else 0L
}

class LessThanPacket(version: Int, typeId: Int, children: List<Packet>) : OperatorPacket(version, typeId, children) {
	override fun combineValues(values: List<Long>) = if (values[0] < values[1]) 1L else 0L
}

class EqualToPacket(version: Int, typeId: Int, children: List<Packet>) : OperatorPacket(version, typeId, children) {
	override fun combineValues(values: List<Long>) = if (values[0] == values[1]) 1L else 0L
}