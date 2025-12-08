package eu.janvdb.aoc2025.day08

import eu.janvdb.aocutil.kotlin.point3d.Point3D
import eu.janvdb.aocutil.kotlin.readLines

//const val FILENAME = "input08-test.txt"
//const val NUMBER_TO_CONNECT = 10

const val FILENAME = "input08.txt"
const val NUMBER_TO_CONNECT = 1000

const val NUMBER_TO_MULTIPLY = 3

fun main() {
    val boxes = readLines(2025, FILENAME).map(JunctionBox::parse)

    val pairs = (0..<boxes.size)
        .flatMap { i1 -> (i1 + 1..<boxes.size).map { i2 -> JunctionBoxPair(boxes[i1], boxes[i2]) } }
        .sortedWith { box1, box2 -> box1.distanceSquared.compareTo(box2.distanceSquared) }

    part1(pairs)
    part2(pairs, boxes.size)
}

private fun part1(pairs: List<JunctionBoxPair>) {
    var circuits = setOf<Circuit>()
    pairs.take(NUMBER_TO_CONNECT).forEach { circuits = addToCircuits(circuits, it) }
    val sizes = circuits.map { it.size }.sortedDescending()
    println(sizes.take(NUMBER_TO_MULTIPLY).fold(1L) { a, b -> a * b })
}

private fun part2(pairs: List<JunctionBoxPair>, numberOfBoxes: Int) {
    var circuits = setOf<Circuit>()
    for (pair in pairs) {
        circuits = addToCircuits(circuits, pair)
        if (circuits.size == 1 && circuits.first().size == numberOfBoxes) {
            println(1L * pair.box1.location.x * pair.box2.location.x)
            break
        }
    }
}

fun addToCircuits(circuits: Set<Circuit>, pair: JunctionBoxPair): Set<Circuit> {
    val circuit1 = circuits.find { it.contains(pair.box1) } ?: Circuit.forBox(pair.box1)
    val circuit2 = circuits.find { it.contains(pair.box2) } ?: Circuit.forBox(pair.box2)
    return circuits - circuit1 - circuit2 + circuit1.join(circuit2)
}

data class JunctionBox(val location: Point3D) {
    companion object {
        fun parse(line: String): JunctionBox {
            val values = line.split(",")
            return JunctionBox(Point3D(values[0].toInt(), values[1].toInt(), values[2].toInt()))
        }
    }
}

data class JunctionBoxPair(val box1: JunctionBox, val box2: JunctionBox) {
    val distanceSquared = box1.location.distanceSquared(box2.location)
}

data class Circuit(val boxes: Set<JunctionBox>) {
    val size = boxes.size

    fun join(circuit: Circuit) = Circuit(boxes + circuit.boxes)
    fun contains(box: JunctionBox) = box in this.boxes

    companion object {
        fun forBox(box: JunctionBox): Circuit {
            return Circuit(setOf(box))
        }
    }
}
