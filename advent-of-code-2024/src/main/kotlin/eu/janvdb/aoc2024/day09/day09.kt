package eu.janvdb.aoc2024.day09

import eu.janvdb.aocutil.kotlin.readLines
import kotlin.math.min
import kotlin.streams.toList

//const val FILENAME = "input09-test.txt"
const val FILENAME = "input09.txt"

fun main() {
    val data = readLines(2024, FILENAME).joinToString("")
    val diskMap = DiskMap.fromString(data)
    println(diskMap.compact(true).checksum())
    println(diskMap.compact(false).checksum())
}

data class DiskMap(val blocks: List<Block>) {

    fun compact(allowFragmentation: Boolean): DiskMap {
        val newBlocks = blocks.toMutableList()

        fun move(fromIndex: Int, toIndex: Int): Int {
            val fromBlock = newBlocks[fromIndex]
            val toBlock = newBlocks[toIndex]

            val dataToMove = min(fromBlock.size, toBlock.size)
            newBlocks[toIndex] = Block.used(dataToMove, fromBlock.fileIndex!!)
            newBlocks[fromIndex] = Block.free(dataToMove)

            if (dataToMove < fromBlock.size) {
                newBlocks.add(fromIndex + 1, Block.used(fromBlock.size - dataToMove, fromBlock.fileIndex))
            }
            if (dataToMove < toBlock.size) {
                newBlocks.add(toIndex + 1, Block.free(toBlock.size - dataToMove))
            }

            return dataToMove
        }

        val filesToMove = blocks.reversed().filter { it.isUsed() }.map { it.fileIndex }.toMutableList()

        while (filesToMove.isNotEmpty()) {
            val fileToMove = filesToMove.removeAt(0)!!

            while (true) {
                val fromIndex = newBlocks.indices.findLast { newBlocks[it].fileIndex == fileToMove }!!
                val dataToMove = newBlocks[fromIndex].size
                val toIndex = newBlocks.indexOfFirst { it.isFree() && (allowFragmentation || it.size >= newBlocks[fromIndex].size) }
                if (toIndex > fromIndex || toIndex == -1) break
                val moved = move(fromIndex, toIndex)
                if (moved == dataToMove) break
            }
        }

        val result = DiskMap(newBlocks)
        return result
    }


    fun checksum(): Long {
        var position = 0L
        return blocks.asSequence()
            .map {
                val result = if (it.isUsed()) it.fileIndex!! * it.size * (2 * position + it.size - 1) / 2 else 0
                position += it.size
                result
            }
            .sum()
    }

    override fun toString(): String = blocks.joinToString("") { (it.fileIndex?.toString() ?: ".").repeat(it.size) }

    companion object {
        fun fromString(input: String): DiskMap {
            val sizes = input.chars().map(Character::getNumericValue).toList()
            val blocks = sizes.mapIndexed { index, size ->
                if (index % 2 == 0) Block.used(size, index / 2) else Block.free(size)
            }
            return DiskMap(blocks)
        }
    }
}

data class Block(val type: BlockType, val size: Int, val fileIndex: Int?) {
    fun isUsed() = type == BlockType.USED
    fun isFree() = type == BlockType.FREE

    companion object {
        fun free(size: Int) = Block(BlockType.FREE, size, null)
        fun used(size: Int, fileIndex: Int) = Block(BlockType.USED, size, fileIndex)
    }
}

enum class BlockType { FREE, USED }
