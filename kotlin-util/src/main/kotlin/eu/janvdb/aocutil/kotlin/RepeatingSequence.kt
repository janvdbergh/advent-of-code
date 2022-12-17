package eu.janvdb.aocutil.kotlin

class RepeatingSequence<T>(val values: List<T>) {
	private var index = -1
	fun next(): T {
		index = (index + 1) % values.size
		return values[index]
	}
}