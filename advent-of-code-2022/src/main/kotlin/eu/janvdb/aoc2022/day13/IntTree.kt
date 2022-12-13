package eu.janvdb.aoc2022.day13

interface TreeItem : Comparable<TreeItem>

class TreeLeaf(val value: Int) : TreeItem {
	override fun compareTo(other: TreeItem): Int {
		if (other is TreeLeaf) {
			return this.value.compareTo(other.value)
		}
		return TreeNode(listOf(this)).compareTo(other)
	}

	override fun toString(): String = value.toString()
}

class TreeNode(val items: List<TreeItem>) : TreeItem {
	override fun compareTo(other: TreeItem): Int {
		return if (other is TreeNode) {
			this.compareTo(other)
		} else {
			this.compareTo(TreeNode(listOf(other)))
		}
	}

	private fun compareTo(other: TreeNode): Int {
		var index = 0
		while (index < this.items.size && index < other.items.size) {
			val value = this.items[index].compareTo(other.items[index])
			if (value!=0) return value

			index++
		}

		return this.items.size.compareTo(other.items.size)
	}

	override fun toString(): String = items.toString()
}

fun String.toTreeNode(): TreeItem {
	fun readTreeItem(from: Int): Pair<TreeItem, Int> {
		fun readLeaf(from: Int): Pair<TreeItem, Int> {
			var index = from
			var value = 0
			while (this[index].isDigit()) {
				value = value * 10 + (this[index] - '0')
				index++
			}
			return Pair(TreeLeaf(value), index)
		}

		fun readNode(from: Int): Pair<TreeItem, Int> {
			var index = from + 1
			val items = mutableListOf<TreeItem>()
			while (this[index] != ']') {
				if (this[index] == ',') {
					index++
				} else {
					val pair = readTreeItem(index)
					items.add(pair.first)
					index = pair.second
				}
			}

			return Pair(TreeNode(items), index + 1)
		}

		return if (this[from] == '[') readNode(from) else readLeaf(from)
	}

	return readTreeItem(0).first
}