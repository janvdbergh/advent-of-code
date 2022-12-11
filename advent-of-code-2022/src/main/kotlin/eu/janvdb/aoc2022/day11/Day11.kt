package eu.janvdb.aoc2022.day11

// TEST INPUT
val MONKEYS_TEST = Monkeys(
	listOf(
//Monkey 0:
//Starting items: 79, 98
//Operation: new = old * 19
//Test: divisible by 23
//If true: throw to monkey 2
//If false: throw to monkey 3
		Monkey(listOf(79, 98), { it * 19 }, 23, 2, 3),
//
//Monkey 1:
//Starting items: 54, 65, 75, 74
//Operation: new = old + 6
//Test: divisible by 19
//If true: throw to monkey 2
//If false: throw to monkey 0
		Monkey(listOf(54, 65, 75, 74), { it + 6 }, 19, 2, 0),
//
//Monkey 2:
//Starting items: 79, 60, 97
//Operation: new = old * old
//Test: divisible by 13
//If true: throw to monkey 1
//If false: throw to monkey 3
		Monkey(listOf(79, 60, 97), { it * it }, 13, 1, 3),
//
//Monkey 3:
//Starting items: 74
//Operation: new = old + 3
//Test: divisible by 17
//If true: throw to monkey 0
//If false: throw to monkey 1
		Monkey(listOf(74), { it + 3 }, 17, 0, 1),
	)
)

// REAL INPUT
val MONKEYS_REAL = Monkeys(
	listOf(
//Monkey 0:
//Starting items: 64
//Operation: new = old * 7
//Test: divisible by 13
//If true: throw to monkey 1
//If false: throw to monkey 3
		Monkey(listOf(64), { it * 7 }, 13, 1, 3),
//
//Monkey 1:
//Starting items: 60, 84, 84, 65
//Operation: new = old + 7
//Test: divisible by 19
//If true: throw to monkey 2
//If false: throw to monkey 7
		Monkey(listOf(60, 84, 84, 65), { it + 7 }, 19, 2, 7),
//
//Monkey 2:
//Starting items: 52, 67, 74, 88, 51, 61
//Operation: new = old * 3
//Test: divisible by 5
//If true: throw to monkey 5
//If false: throw to monkey 7
		Monkey(listOf(52, 67, 74, 88, 51, 61), { it * 3 }, 5, 5, 7),
//
//Monkey 3:
//Starting items: 67, 72
//Operation: new = old + 3
//Test: divisible by 2
//If true: throw to monkey 1
//If false: throw to monkey 2
		Monkey(listOf(67, 72), { it + 3 }, 2, 1, 2),
//
//Monkey 4:
//Starting items: 80, 79, 58, 77, 68, 74, 98, 64
//Operation: new = old * old
//Test: divisible by 17
//If true: throw to monkey 6
//If false: throw to monkey 0
		Monkey(listOf(80, 79, 58, 77, 68, 74, 98, 64), { it * it }, 17, 6, 0),
//
//Monkey 5:
//Starting items: 62, 53, 61, 89, 86
//Operation: new = old + 8
//Test: divisible by 11
//If true: throw to monkey 4
//If false: throw to monkey 6
		Monkey(listOf(62, 53, 61, 89, 86), { it + 8 }, 11, 4, 6),
//
//Monkey 6:
//Starting items: 86, 89, 82
//Operation: new = old + 2
//Test: divisible by 7
//If true: throw to monkey 3
//If false: throw to monkey 0
		Monkey(listOf(86, 89, 82), { it + 2 }, 7, 3, 0),
//
//Monkey 7:
//Starting items: 92, 81, 70, 96, 69, 84, 83
//Operation: new = old + 4
//Test: divisible by 3
//If true: throw to monkey 4
//If false: throw to monkey 5
		Monkey(listOf(92, 81, 70, 96, 69, 84, 83), { it + 4 }, 3, 4, 5),
	)
)

//val MONKEYS = MONKEYS_TEST
val MONKEYS = MONKEYS_REAL

const val DIVISOR = 1L
const val NUMBER_OF_ROUNDS = 10_000

fun main() {
	for (i in 0 until NUMBER_OF_ROUNDS) {
		MONKEYS.doRound()

		if (i==0 || i == 19 || (i % 1000 == 999)) {
			val output = MONKEYS.monkeys.joinToString { it.itemsInvestigated.toString() }
			println("${i + 1}\t\t$output")
		}
	}

	println(MONKEYS.getScore())
}