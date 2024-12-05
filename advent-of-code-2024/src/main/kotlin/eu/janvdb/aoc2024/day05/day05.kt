package eu.janvdb.aoc2024.day05

import eu.janvdb.aocutil.kotlin.readGroupedLines

//const val FILENAME = "input05-test.txt"
const val FILENAME = "input05.txt"

fun main() {
    val blocks = readGroupedLines(2024, FILENAME)
    val orderings = PageOrdering.fromStrings(blocks[0])
    val pageLists = blocks[1].map { PageList.fromString(it) }

    val sortedCorrectly = pageLists.filter { orderings.isSortedCorrectly(it) }
    val middlePages = sortedCorrectly.map { it.middlePage().id }
    println(middlePages.sum())

    val notSortedCorrectly = pageLists - sortedCorrectly;
    val resorted = notSortedCorrectly.map { orderings.sort(it) }
    val middlePages2 = resorted.map { it.middlePage().id }
    println(middlePages2.sum())
}

data class Page(val id: Int)

data class PageList(val pages: List<Page>) {
    fun middlePage(): Page {
        return pages[pages.size / 2]
    }

    companion object {
        fun fromString(string: String): PageList {
            val pages = string.split(",").map { Page(it.toInt()) }
            return PageList(pages)
        }
    }
}

data class Ordering(val id1: Int, val id2: Int) {
    companion object {
        fun fromString(string: String): Ordering {
            val parts = string.split("|")
            return Ordering(parts[0].toInt(), parts[1].toInt())
        }
    }
}

data class PageOrdering(val orderings: Set<Ordering>) : Comparator<Page> {

    fun isSortedCorrectly(pageList: PageList): Boolean {
        for (i in 0 until pageList.pages.size) {
            for (j in i + 1 until pageList.pages.size) {
                if (!orderings.contains(Ordering(pageList.pages[i].id, pageList.pages[j].id))) {
                    return false
                }
            }
        }
        return true
    }

    fun sort(pageList: PageList): PageList {
        val sortedPages = pageList.pages.sortedWith(this)
        return PageList(sortedPages)
    }

    override fun compare(o1: Page?, o2: Page?): Int {
        if (o1 == null) return -1
        if (o2 == null) return 1
        if (orderings.contains(Ordering(o1.id, o2.id))) return -1
        return 1
    }

    companion object {
        fun fromStrings(strings: List<String>): PageOrdering {
            val orderings = strings.map { Ordering.fromString(it) }.toSet()
            return PageOrdering(orderings)
        }
    }
}
