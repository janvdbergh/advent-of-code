package eu.janvdb.aocutil.kotlin

import java.util.*

data class CombinedRange internal constructor(val ranges: SortedSet<SimpleRange>): Iterable<SimpleRange> {

    constructor() : this(sortedSetOf())

    fun add(range: SimpleRange): CombinedRange {
        if (range.isEmpty()) return this

        val newRanges = sortedSetOf<SimpleRange>()
        var currentRange = range
        ranges.forEach {
            if (currentRange.canJoin(it)) {
                currentRange = currentRange.join(it)
            } else {
                newRanges.add(it)
            }
        }
        newRanges.add(currentRange)

        return CombinedRange(newRanges)
    }

    override fun iterator() = ranges.iterator()
}