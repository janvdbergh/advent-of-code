package eu.janvdb.aocutil.kotlin

import kotlin.math.max
import kotlin.math.min

data class SimpleRange(val start: Long, val endInclusive: Long) : Comparable<SimpleRange> {
    fun overlapsWith(other: SimpleRange): Boolean {
        if (endInclusive < other.start) return false
        if (other.endInclusive < start) return false
        return true
    }

    fun canJoin(other: SimpleRange): Boolean {
        if (endInclusive < other.start - 1) return false
        if (other.endInclusive < start - 1) return false
        return true
    }

    fun intersectWith(other: SimpleRange): SimpleRange {
        return SimpleRange(max(this.start, other.start), min(this.endInclusive, other.endInclusive))
    }

    fun join(other: SimpleRange): SimpleRange {
        assert(canJoin(other))
        return SimpleRange(min(start, other.start), max(endInclusive, other.endInclusive))
    }

    fun subtract(other: SimpleRange): CombinedRange {
        if (overlapsWith(other)) {
            return CombinedRange(
                sequenceOf(SimpleRange(start, other.start - 1), SimpleRange(other.endInclusive + 1, endInclusive))
                    .filter { !it.isEmpty() }
                    .toSortedSet()
            )
        }
        return CombinedRange(sortedSetOf(this))
    }

    fun move(steps: Long): SimpleRange {
        return SimpleRange(start + steps, endInclusive + steps)
    }

    fun isEmpty(): Boolean {
        return endInclusive < start
    }

    override fun compareTo(other: SimpleRange): Int {
        if (start < other.start) return -1
        if (start > other.start) return 1
        if (endInclusive < other.endInclusive) return -1
        if (endInclusive > other.endInclusive) return 1
        return 0
    }
}