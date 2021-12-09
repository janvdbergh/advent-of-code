package eu.janvdb.aoc2021.day09

import eu.janvdb.aocutil.kotlin.point2d.Point2D
import eu.janvdb.aocutil.kotlin.readLines
import java.util.*

private const val FILENAME = "input09.txt"

fun main() {
	val cave = Cave.read()

	val riskLevel = cave.lowPoints()
		.map { cave.heightAt(it) + 1 }
		.sum()
	println(riskLevel)

	val score2 = cave.bassins()
		.map { it.size }
		.sortedDescending()
		.take(3)
		.fold(1) { acc, it -> acc * it }
	println(score2)
}

private const val MAX_HEIGHT = 9

data class Cave(val heights: List<List<Int>>) {
	val width = heights[0].size
	val height = heights.size

	fun bassins(): Set<Set<Point2D>> {
		fun expandToBassin(origin: Point2D): Set<Point2D> {
			val bassin = mutableSetOf<Point2D>()
			val toDo = LinkedList<Point2D>()
			toDo.addFirst(origin)

			while (!toDo.isEmpty()) {
				val point = toDo.removeAt(0)
				if (heightAt(point) != MAX_HEIGHT && !bassin.contains(point)) {
					bassin.add(point)
					toDo.addLast(point.left())
					toDo.addLast(point.right())
					toDo.addLast(point.up())
					toDo.addLast(point.down())
				}
			}

			return bassin
		}

		return lowPoints().map(::expandToBassin).toSet()
	}

	fun lowPoints() = points().filter(this::isLowPoint)

	private fun points(): Sequence<Point2D> {
		return (0..height).asSequence()
			.flatMap { y -> (0..width).asSequence().map { x -> Point2D(x, y) } }
	}

	private fun isLowPoint(point: Point2D): Boolean {
		val height = heightAt(point)
		return height < heightAt(point.left()) && height < heightAt(point.right())
				&& height < heightAt(point.up()) && height < heightAt(point.down())
	}

	fun heightAt(point: Point2D): Int {
		if (point.x < 0 || point.x >= width || point.y < 0 || point.y >= height) return MAX_HEIGHT
		return heights[point.y][point.x]
	}

	companion object {
		fun read(): Cave {
			val heights = readLines(2021, FILENAME)
				.map { line -> line.toCharArray().map { ch -> ch - '0' }.toList() }
			return Cave(heights)
		}
	}
}
