package eu.janvdb.aoc2023.day22

import eu.janvdb.aocutil.kotlin.point3d.Point3D
import eu.janvdb.aocutil.kotlin.readLines
import kotlin.math.max
import kotlin.math.min

//const val FILENAME = "input22-test.txt"
const val FILENAME = "input22.txt"

fun main() {
	val bricks = readLines(2023, FILENAME).mapIndexed { index, it -> Brick.parse(index, it) }
	val puzzle = Puzzle.build(bricks)

	val blocksToDisintegrate = puzzle.getBlocksToDisintegrate()
	println(bricks.size - blocksToDisintegrate.size)

	val counts = blocksToDisintegrate.sumOf { puzzle.removeBrickAndCountMovedBlocks(it) }
	println(counts)
}

data class Puzzle(val bricks: Set<Brick>) {

	fun getBlocksToDisintegrate(): List<Brick> {
		val brickSupport = bricks.flatMap { brick -> bricks.filter { brick.supports(it) }.map { Pair(brick, it) } }

		val bricksToDisintegrate = bricks.filter { brick ->
			!brickSupport.asSequence()
				.filter { it.first == brick }.map { it.second }
				.map { supported -> brickSupport.asSequence().filter { it.second == supported && it.first != brick } }
				.none { it.count() == 0 }
		}

		return bricksToDisintegrate
	}

	fun removeBrickAndCountMovedBlocks(brick: Brick): Int {
		val newBricks = bricks - brick
		val newPuzzle = build(newBricks)
		val movedBlocks = newPuzzle.bricks - bricks
		return movedBlocks.size
	}

	companion object {
		fun build(bricks: Collection<Brick>): Puzzle {
			val movedBricks = mutableSetOf<Brick>()
			val points = mutableSetOf<Point3D>()

			fun intersectsWith(brick: Brick): Boolean {
				return brick.getPoints().any { it in points }
			}

			fun dropAndAdd(brick: Brick) {
				var previous = brick
				var current = brick.moveDown()
				while (current.from.z > 0 && !intersectsWith(current)) {
					previous = current
					current = current.moveDown()
				}

				movedBricks.add(previous)
				points.addAll(previous.getPoints())
			}

			bricks.sortedBy { it.from.z }.forEach(::dropAndAdd)

			return Puzzle(movedBricks)
		}
	}
}

data class Brick(val index: Int, val from: Point3D, val to: Point3D) {
	private val minX = min(from.x, to.x)
	private val maxX = max(from.x, to.x)
	private val minY = min(from.y, to.y)
	private val maxY = max(from.y, to.y)

	fun getPoints(): Sequence<Point3D> {
		return (min(from.z, to.z)..max(from.z, to.z)).asSequence()
			.flatMap { z ->
				(min(from.y, to.y)..max(from.y, to.y)).asSequence()
					.flatMap { y -> (min(from.x, to.x)..max(from.x, to.x)).asSequence().map { x -> Point3D(x, y, z) } }
			}
	}

	fun moveDown(steps: Int = 1): Brick {
		return Brick(index, from.move(0, 0, -steps), to.move(0, 0, -steps))
	}

	fun supports(other: Brick): Boolean {
		if (this.to.z != other.from.z - 1) return false
		if (other.minX > this.maxX) return false
		if (other.maxX < this.minX) return false
		if (other.minY > this.maxY) return false
		if (other.maxY < this.minY) return false

		return true
	}

	companion object {
		private val REGEX = Regex("[,~]")

		fun parse(index: Int, line: String): Brick {
			val parts = line.split(REGEX)
			val from = Point3D(parts[0].toInt(), parts[1].toInt(), parts[2].toInt())
			val to = Point3D(parts[3].toInt(), parts[4].toInt(), parts[5].toInt())
			return if (to.z < from.z) Brick(index, to, from) else Brick(index, from, to)
		}
	}
}