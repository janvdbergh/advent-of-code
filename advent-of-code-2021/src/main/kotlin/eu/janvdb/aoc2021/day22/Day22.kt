package eu.janvdb.aoc2021.day22

import eu.janvdb.aocutil.kotlin.readLines
import kotlin.math.max
import kotlin.math.min

const val FILENAME = "input22.txt"

fun main() {
	val instructions = readLines(2021, FILENAME).map(Instruction::parse)

	part(instructions.filter { it.region.xMin>=-50 && it.region.xMax<=50 && it.region.yMin>=-50 && it.region.yMax<=50 && it.region.zMin>=-50 && it.region.zMax<=50})
	part(instructions)
}

private fun part(instructions: List<Instruction>) {
	var currentRegion = CombinedRegion()
	instructions.forEach { currentRegion = currentRegion.apply(it) }
	println(currentRegion.volume)
}

data class Region(val xMin: Int, val xMax: Int, val yMin: Int, val yMax: Int, val zMin: Int, val zMax: Int) {
	val notEmpty get() = xMin <= xMax && yMin <= yMax && zMin <= zMax

	val volume get() = 1L * (xMax - xMin + 1) * (yMax - yMin + 1) * (zMax - zMin + 1)

	fun subtract(other: Region): Sequence<Region> {
		if (other.xMin > xMax || other.xMax < xMin || other.yMin > yMax || other.yMax < yMin || other.zMin > zMax || other.zMax < zMin)
			return sequenceOf(this)

		return sequenceOf(
			Region(xMin, other.xMin - 1, yMin, yMax, zMin, zMax),
			Region(other.xMax + 1, xMax, yMin, yMax, zMin, zMax),
			Region(max(other.xMin, xMin), min(other.xMax, xMax), yMin, other.yMin - 1, zMin, zMax),
			Region(max(other.xMin, xMin), min(other.xMax, xMax), other.yMax + 1, yMax, zMin, zMax),
			Region(max(other.xMin, xMin), min(other.xMax, xMax), max(other.yMin, yMin), min(other.yMax, yMax), zMin, other.zMin - 1),
			Region(max(other.xMin, xMin), min(other.xMax, xMax), max(other.yMin, yMin), min(other.yMax, yMax), other.zMax + 1, zMax),
		).filter(Region::notEmpty)
	}

	fun subtract(others: List<Region>): List<Region> {
		var current = listOf(this)
		others.forEach { other -> current = current.flatMap { it.subtract(other) } }

		return current
	}
}

data class CombinedRegion(val regions: List<Region> = listOf()) {
	val volume get() = regions.map(Region::volume).sum()

	fun apply(instruction: Instruction): CombinedRegion {
		val newRegions = if (instruction.isAdd)
			regions + instruction.region.subtract(regions)
		else
			regions.flatMap { it.subtract(instruction.region) }

		return CombinedRegion(newRegions)
	}
}

class Instruction(val region: Region, val isAdd: Boolean) {
	companion object {
		val regex = Regex("(on|off) x=(-?\\d+)..(-?\\d+),y=(-?\\d+)..(-?\\d+),z=(-?\\d+)..(-?\\d+)")

		fun parse(line: String): Instruction {
			val matchResult = regex.matchEntire(line)!!

			return Instruction(
				Region(
					matchResult.groupValues[2].toInt(), matchResult.groupValues[3].toInt(),
					matchResult.groupValues[4].toInt(), matchResult.groupValues[5].toInt(),
					matchResult.groupValues[6].toInt(), matchResult.groupValues[7].toInt()
				),
				isAdd = matchResult.groupValues[1] == "on"
			)
		}
	}
}