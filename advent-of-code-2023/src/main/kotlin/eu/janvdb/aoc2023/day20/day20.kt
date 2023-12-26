package eu.janvdb.aoc2023.day20

import eu.janvdb.aocutil.kotlin.gcd
import eu.janvdb.aocutil.kotlin.readLines
import java.util.*

//const val FILENAME = "input20-test.txt"
//const val OUTPUT_NAME = "output"
const val FILENAME = "input20.txt"
const val OUTPUT_NAME = "rx"

fun main() {
	val system = System.parse(readLines(2023, FILENAME))

	part1(system)
	part2(system)
}

private fun part1(system: System) {
	system.reset()
	val counts = (1..1000).asSequence()
		.map { system.signal() }
		.fold(Pair(0, 0)) { acc, pair -> Pair(acc.first + pair.first, acc.second + pair.second) }

	println(counts.first * counts.second)
}

private fun part2(system: System) {
	// intermediary end nodes: cl, rp, lb, nj, found by inspecting the input

	val result = sequenceOf("cl", "rp", "lb", "nj")
		.map { system.subSystem(it).getRepetition() }
		.fold(1L) { acc, it -> acc * it / gcd(acc, it) }

	println(result)
}

enum class SignalType {
	LOW, HIGH;

	operator fun not() = if (this == LOW) HIGH else LOW
}

data class Signal(val from: String, val to: String, val type: SignalType)

data class System(val modules: Map<String, Module>, val outputName: String) {
	val endNode = modules[outputName]!!
	var buttonPresses = 0

	fun reset() = modules.values.forEach(Module::reset)

	fun signal(): Pair<Int, Int> {
		buttonPresses++

		val todo = LinkedList<Signal>()
		var count = Pair(0, 0)
		todo.add(Signal("button", "broadcaster", SignalType.LOW))

		while (!todo.isEmpty()) {
			val signal = todo.removeFirst()
			count = if (signal.type == SignalType.LOW)
				Pair(count.first + 1, count.second)
			else
				Pair(count.first, count.second + 1)

			val newSignals = modules[signal.to]!!.trigger(signal, buttonPresses)
			todo.addAll(newSignals)
		}

		return count
	}

	fun subSystem(newOutputName: String): System {
		val newEndModule = this.modules[newOutputName]!!
		val newModules = mutableSetOf(newEndModule)
		val todo = LinkedList<Module>()
		todo.add(newEndModule)

		while (!todo.isEmpty()) {
			val module = todo.removeFirst()
			modules.values.asSequence()
				.filter { it.outputs.contains(module.name) }
				.filter { !newModules.contains(it) }
				.forEach {
					newModules.add(it)
					todo.add(it)
				}
		}

		val newModuleNames = newModules.map { it.name }.toSet()
		val limitedModules = newModules.map { it.limitOutputsTo(newModuleNames) }

		return System(limitedModules.associateBy { it.name }, newOutputName)
	}

	fun getRepetition(): Long {
		val previousPresses = mutableListOf(0)
		var previousDifference = 0

		while (true) {
			signal()

			if (endNode.lowSignals.isNotEmpty()) {
				val currentPress = endNode.lowSignals.last().second
				val currentDifference = currentPress - previousPresses.last()
				if (currentDifference != 0) {
					if (currentDifference == previousDifference && currentDifference > 1) {
						return currentDifference.toLong()
					}
					previousPresses += currentPress
					previousDifference = currentDifference
				}
			}
		}
	}

	companion object {
		fun parse(lines: List<String>): System {
			val modules =
				lines.map { parse(it) }.associateBy { it.name } + Pair(OUTPUT_NAME, Output(OUTPUT_NAME))

			return System(modules, OUTPUT_NAME)
		}

		private fun parse(line: String): Module {
			val parts = line.split(" -> ")
			val outputs = parts[1].split(", ")

			return when {
				parts[0].startsWith('%') -> FlipFlop(parts[0].substring(1), outputs)
				parts[0].startsWith('&') -> Conjunction(parts[0].substring(1), outputs)
				else -> Broadcaster(parts[0], outputs)
			}
		}
	}
}

abstract class Module(val name: String, val outputs: List<String>) {
	val signals = mutableListOf<Pair<SignalType, Int>>()
	val lowSignals = mutableListOf<Pair<SignalType, Int>>()

	open fun reset() {
		signals.clear()
		lowSignals.clear()
	}

	open fun trigger(signal: Signal, buttonPress: Int): Sequence<Signal> {
		if (signal.type == SignalType.LOW) {
			lowSignals.add(Pair(signal.type, buttonPress))
		}
		signals.add(Pair(signal.type, buttonPress))
		return sequenceOf()
	}

	abstract fun limitOutputsTo(moduleNames: Set<String>): Module
}

class FlipFlop(name: String, outputs: List<String>) : Module(name, outputs) {
	private var state = SignalType.LOW

	override fun reset() {
		super.reset()
		state = SignalType.LOW
	}

	override fun trigger(signal: Signal, buttonPress: Int): Sequence<Signal> {
		super.trigger(signal, buttonPress)

		if (signal.type == SignalType.HIGH) return sequenceOf()

		state = !state
		return outputs.asSequence().map { Signal(name, it, state) }
	}

	override fun limitOutputsTo(moduleNames: Set<String>): Module {
		val newOutputs = outputs.intersect(moduleNames)
		return FlipFlop(name, newOutputs.toList())
	}

	override fun toString() = "%($state) -> [$outputs]"
}

class Conjunction(name: String, outputs: List<String>) : Module(name, outputs) {
	private val inputValues = mutableMapOf<String, SignalType>()

	override fun reset() {
		super.reset()
		inputValues.clear()
	}

	override fun trigger(signal: Signal, buttonPress: Int): Sequence<Signal> {
		super.trigger(signal, buttonPress)

		inputValues[signal.from] = signal.type

		val allHigh = inputValues.values.all { it == SignalType.HIGH }
		val value = if (allHigh) SignalType.LOW else SignalType.HIGH
		return outputs.asSequence().map { Signal(name, it, value) }
	}

	override fun limitOutputsTo(moduleNames: Set<String>): Module {
		val newOutputs = outputs.intersect(moduleNames)
		return Conjunction(name, newOutputs.toList())
	}

	override fun toString() = "&($inputValues) -> [$outputs]"
}

class Broadcaster(name: String, outputs: List<String>) : Module(name, outputs) {
	override fun trigger(signal: Signal, buttonPress: Int): Sequence<Signal> {
		super.trigger(signal, buttonPress)

		return outputs.asSequence().map { Signal(name, it, signal.type) }
	}

	override fun limitOutputsTo(moduleNames: Set<String>): Module {
		val newOutputs = outputs.intersect(moduleNames)
		return Broadcaster(name, newOutputs.toList())
	}

	override fun toString() = "()) -> [$outputs]"
}

class Output(name: String) : Module(name, listOf()) {
	override fun limitOutputsTo(moduleNames: Set<String>) = this

	override fun toString() = "()"
}