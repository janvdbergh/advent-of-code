package eu.janvdb.aoc2025.day10

import com.microsoft.z3.Context
import com.microsoft.z3.IntExpr
import com.microsoft.z3.IntNum
import com.microsoft.z3.Status
import eu.janvdb.aocutil.kotlin.readLines
import java.nio.file.Paths
import java.util.*

//const val FILENAME = "input10-test.txt"
const val FILENAME = "input10.txt"

fun main() {
    loadZ3Native()
    val machines = readLines(2025, FILENAME).map(Machine::parse)

    val result1 = machines.sumOf { it.findNumberOfToggles1() }
    println(result1)

    val result2 = machines.sumOf { it.findNumberOfToggles2() }
    println(result2)
}

private fun loadZ3Native() {
    try {
        val libDir = Paths.get("").toAbsolutePath().resolve("lib")
        val candidates = listOf("libz3.dylib", "libz3java.dylib")
        for (name in candidates) {
            System.load(libDir.resolve(name).toString())
        }
    } catch (e: Throwable) {
        throw IllegalStateException("Failed to load Z3 native libraries.", e)
    }
}

data class Button(val size: Int, val toggles: Set<Int>) {
    companion object {
        fun parse(size: Int, input: String): Button {
            val digits = input.substring(1, input.length - 1).split(",").map(String::toInt).toSet()
            return Button(size, digits)
        }
    }
}

data class Machine(val requiredLights: List<Boolean>, val requiredJoltage: List<Int>, val buttons: List<Button>) {
    fun findNumberOfToggles1(): Int {
        Context().use { context ->
            val buttonVariables =
                buttons.mapIndexed { index, button -> Pair(button, context.mkConst("x$index", context.mkIntSort()) as IntExpr?) }.toMap()

            val equations1 = requiredLights.mapIndexed { index, value ->
                val rh = context.mkInt(if (value) 1 else 0)
                val selectedButtons = buttons.filter { it.toggles.contains(index) }.map { buttonVariables[it]!! }.toTypedArray()
                val lh = context.mkMod(context.mkAdd(*selectedButtons), context.mkInt(2))
                context.mkEq(lh, rh)
            }

            val equations2 = buttonVariables.values.map { button ->
                context.mkGe(button, context.mkInt(0))
            }

            val equations = equations1 + equations2

            val optimize = context.mkOptimize()
            equations.forEach { optimize.Add(it) }

            val buttonSum = context.mkAdd(*buttonVariables.values.toTypedArray())
            optimize.MkMinimize(buttonSum)

            if (optimize.Check() === Status.SATISFIABLE) {
                val model = optimize.model
                val values = buttons.map { button -> model.evaluate(buttonVariables[button]!!, true) as IntNum }
                println(values)
                return values.sumOf { it.int }
            }

            throw IllegalStateException("No solution found.")
        }

//        val neighbourFunction: (BitSet) -> Sequence<ShortestPathMove<BitSet, Nothing?>> =
//            { lights -> buttons.asSequence().map { ShortestPathMove(it.toggle(lights), null, 1) } }
//        val result = findShortestPath(startLights, requiredLights, neighbourFunction)!!
//        return result.cost
    }

    fun findNumberOfToggles2(): Int {
        Context().use { context ->
            val buttonVariables =
                buttons.mapIndexed { index, button -> Pair(button, context.mkConst("x$index", context.mkIntSort()) as IntExpr?) }.toMap()

            val equations1 = requiredJoltage.mapIndexed { index, joltage ->
                val rh = context.mkInt(joltage)
                val selectedButtons = buttons.filter { it.toggles.contains(index) }.map { buttonVariables[it]!! }.toTypedArray()
                val lh = context.mkAdd(*selectedButtons)
                context.mkEq(lh, rh)
            }

            val equations2 = buttonVariables.values.map { button ->
                context.mkGe(button, context.mkInt(0))
            }

            val equations = equations1 + equations2

            val optimize = context.mkOptimize()
            equations.forEach { optimize.Add(it) }

            val buttonSum = context.mkAdd(*buttonVariables.values.toTypedArray())
            optimize.MkMinimize(buttonSum)

            if (optimize.Check() === Status.SATISFIABLE) {
                val model = optimize.model
                val values = buttons.map { button -> model.evaluate(buttonVariables[button]!!, true) as IntNum }
                println(values)
                return values.sumOf { it.int }
            }

            throw IllegalStateException("No solution found.")
        }
    }

    companion object {
        fun parse(line: String): Machine {
            val groups = line.split(" ")
            val numberOfLights = groups[0].length - 2

            val lights = groups[0].substring(1, numberOfLights + 1)
                .mapIndexed { index, ch -> Pair(index, ch == '#') }
                .map { it.second }

            val buttons = groups.drop(1).take(groups.size - 2).map { Button.parse(numberOfLights, it) }
            val joltage = groups.last().substring(1, groups.last().length - 1).split(",").map(String::toInt)

            return Machine(lights, joltage, buttons)
        }
    }
}
