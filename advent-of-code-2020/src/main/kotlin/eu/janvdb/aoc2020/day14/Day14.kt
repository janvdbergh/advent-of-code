package eu.janvdb.aoc2020.day14

import eu.janvdb.aocutil.kotlin.readLines

const val MASK_PREFIX = "mask = "
val MEMORY_REGEX = Regex("mem\\[(\\d+)] = (\\d+)")

fun main() {
	val fileData = readLines(2020, "input14.txt")
	execute(fileData, ::Bitmap1)
	execute(fileData, ::Bitmap2)
}

fun execute(fileData: List<String>, bitmapConstructor: (String) -> Bitmap) {
	var mask: Bitmap? = null
	val memory = mutableMapOf<Long, Long>()
	for (line in fileData) {
		if (line.startsWith(MASK_PREFIX)) {
			mask = bitmapConstructor.invoke(line.substring(MASK_PREFIX.length))
		} else {
			updateMemory(memory, mask!!, line)
		}
	}

	val sum = memory.map { it.value }.sum()
	println(sum)
}

fun updateMemory(memory: MutableMap<Long, Long>, bitmap: Bitmap, line: String) {
	val matchResult = MEMORY_REGEX.matchEntire(line) ?: throw IllegalArgumentException(line)

	val address = matchResult.groupValues[1].toLong()
	val value = matchResult.groupValues[2].toLong()

	bitmap.apply(memory, address, value)
}

interface Bitmap {
	fun apply(memory: MutableMap<Long, Long>, address: Long, value: Long)
}

class Bitmap1(mask: String) : Bitmap {
	private val andVector = mask.replace('X', '1').toLong(2)
	private val orVector = mask.replace('X', '0').toLong(2)

	override fun apply(memory: MutableMap<Long, Long>, address: Long, value: Long) {
		val updatedValue = value and andVector or orVector
		memory[address] = updatedValue
	}
}

class Bitmap2(private val mask: String) : Bitmap {
	private val orVector = mask.replace('X', '0').toLong(2)

	override fun apply(memory: MutableMap<Long, Long>, address: Long, value: Long) {
		val updatedAddress = address or orVector
		apply(memory, updatedAddress, value, mask)
	}

	private fun apply(memory: MutableMap<Long, Long>, address: Long, value: Long, mask: String) {
		val index = mask.indexOf('X')
		if (index == -1) {
			memory[address] = value
		} else {
			val bit = 35 - index
			val newMask = mask.replaceRange(index, index + 1, "0")
			apply(memory, address or (1L shl bit), value, newMask)
			apply(memory, address and (1L shl bit).inv(), value, newMask)
		}
	}
}
