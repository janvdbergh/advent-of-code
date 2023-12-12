package eu.janvdb.aoc2023.day12

import eu.janvdb.aocutil.kotlin.readLines

//private const val FILENAME = "input12-test.txt"
private const val FILENAME = "input12.txt"

private val BLOCK_REGEX = Regex("#+")

fun main() {
    val puzzles = readLines(2023, FILENAME).map { Puzzle.parse(it) }
    val counts = puzzles.map { it.countSolutions() }
    val sum1 = counts.sum()
    println(sum1)

    val puzzles2 = puzzles.map { it.unfold() }
    val counts2 = puzzles2.map { it.countSolutions() }
    val sum2 = counts2.sum()
    println(sum2)
}

private const val REPETITIONS = 5

data class Puzzle(val pattern: String, val blocks: List<Int>) {
    private val cache = mutableMapOf<Pair<String, List<Int>>, Long>()

    fun unfold(): Puzzle {
        val pattern = (1..REPETITIONS).joinToString("?") { pattern }
        val blocks = (1..REPETITIONS).asSequence().flatMap { blocks }.toList()
        return Puzzle(pattern, blocks)
    }

    fun countSolutions(): Long {
        return countSolutions(pattern, blocks)
    }

    private fun countSolutions(pattern: String, blocks: List<Int>): Long {
        val key = Pair(pattern, blocks)
        return cache.getOrPut(key) { doCountSolutions(pattern, blocks) }
    }

    private fun doCountSolutions(pattern: String, blocks: List<Int>): Long {
        fun getCh(i: Int) = if (i < 0 || i >= pattern.length) '.' else pattern[i]

        // Check if still occurrences
        if (blocks.isEmpty()) return if (pattern.none { it == '#' }) 1L else 0L

        // Check if the solution can fit here
        if (pattern.length < blocks.sum() + blocks.size - 1) return 0L

        // Start by finding the largest fixated block
        val largestFixedBlock =
            BLOCK_REGEX.findAll(pattern).map { Pair(it.range.first, it.range.last - it.range.first + 1) }
                .sortedByDescending { it.second }
                .firstOrNull()

        if (largestFixedBlock != null) {
            // Find the blocks that fit and split there
            val possibleBlocks = blocks.mapIndexed { index, occurrence -> Pair(index, occurrence) }
                .filter { it.second >= largestFixedBlock.second }

            return possibleBlocks.sumOf { block ->
                val requiredWildcards = block.second - largestFixedBlock.second
                (largestFixedBlock.first - requiredWildcards..largestFixedBlock.first)
                    .filter { index ->
                        (index..<index + block.second).none { getCh(it) == '.' }
                                && getCh(index - 1) != '#' && getCh(index + block.second) != '#'
                    }
                    .sumOf { index ->
                        val remainderLeft = if (index > 0) pattern.substring(0, index - 1) else ""
                        val remainderRight =
                            if (index + block.second + 1 < pattern.length) pattern.substring(index + block.second + 1) else ""
                        val blocksLeft = if (block.first != 0) blocks.subList(0, block.first) else emptyList()
                        val blocksRight =
                            if (block.first != blocks.size - 1) blocks.subList(
                                block.first + 1,
                                blocks.size
                            ) else emptyList()

                        val solutionsLeft = countSolutions(remainderLeft, blocksLeft)
                        val solutionsRight = countSolutions(remainderRight, blocksRight)

                        solutionsLeft * solutionsRight
                    }
            }
        }

        // Only wildcards
        return countSolutionsWithOnlyWildcards(pattern, blocks)
    }

    private fun countSolutionsWithOnlyWildcards(pattern: String, blocks: List<Int>): Long {
        val key = Pair(pattern, blocks)
        return cache.getOrPut(key) { doCountSolutionsWithOnlyWildcards(pattern, blocks) }
    }

    private fun doCountSolutionsWithOnlyWildcards(pattern: String, blocks: List<Int>): Long {
        // Check if still occurrences
        if (blocks.isEmpty()) return if (pattern.none { it == '#' }) 1L else 0L

        // Check if the solution can fit here
        if (pattern.length < blocks.sum() + blocks.size - 1) return 0L

        // Handle empty case
        val startingWithSpace = countSolutionsWithOnlyWildcards(pattern.substring(1), blocks)
        if (pattern[0] == '.') return startingWithSpace

        // Block starting here should match first block
        val blockSize = blocks[0]
        if (!(0..<blockSize).all { pattern[it] == '?' }) return startingWithSpace

        val newPattern = if (pattern.length != blockSize) pattern.substring(blockSize + 1) else ""
        return startingWithSpace + countSolutionsWithOnlyWildcards(newPattern, blocks.subList(1, blocks.size))
    }

    companion object {
        fun parse(line: String): Puzzle {
            val parts = line.split(" ")
            return Puzzle(parts[0], parts[1].split(",").map { it.toInt() })
        }
    }
}