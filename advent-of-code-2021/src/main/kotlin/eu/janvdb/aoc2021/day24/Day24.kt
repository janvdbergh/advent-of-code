package eu.janvdb.aoc2021.day24

fun main() {
	// 91699394894995
	val result1 = subroutine(9, 1, 6, 9, 9, 3, 9, 4, 8, 9, 4, 9, 9, 5)
	println(result1)

	// 51147191161261
	val result2 = subroutine(5, 1, 1, 4, 7, 1, 9, 1, 1, 6, 1, 2, 6, 1)
	println(result2)
}

fun subroutine(
	i0: Int,
	i1: Int,
	i2: Int,
	i3: Int,
	i4: Int,
	i5: Int,
	i6: Int,
	i7: Int,
	i8: Int,
	i9: Int,
	i10: Int,
	i11: Int,
	i12: Int,
	i13: Int,
): Boolean {
	val result = StringBuilder()
	addLetter(i0, 3, result)                     //					-> 9 | 5
	addLetter(i1, 12, result)                    //					-> 1 | 1
	addLetter(i2, 9, result)                     //					-> 6 | 1
	extracted2(i3, 6, 12, result)   // i3 == i2 + 3		-> 9 | 4
	addLetter(i4, 2, result)                     //					-> 9 | 7
	extracted2(i5, 8, 1, result)    // i5 == i4 - 6		-> 3 | 1
	extracted2(i6, 4, 1, result)    // i6 == i1 + 8		-> 9 | 9
	addLetter(i7, 13, result)                    //					-> 4 | 1
	addLetter(i8, 1, result)                     //					-> 8 | 1
	addLetter(i9, 6, result)                     //					-> 9 | 6
	extracted2(i10, 11, 2, result)  // i10 = i9 - 5		-> 4 | 1
	extracted2(i11, 0, 11, result)  // i11 = i8 + 1		-> 9 | 2
	extracted2(i12, 8, 10, result)  // i12 = i7 + 5		-> 9 | 6
	extracted2(i13, 7, 3, result)   // i13 = i0 - 4		-> 5 | 1

	println(result)
	return result.isEmpty()
}

private fun addLetter(input: Int, addFactor: Int, result: StringBuilder) {
	result.append(toLetter(input + addFactor))
}

private fun extracted2(input: Int, compareFactor: Int, addFactor: Int, result: StringBuilder) {
	if (result.lastOrNull()!! == toLetter(compareFactor + input)) {
		result.deleteCharAt(result.length - 1)
	} else {
		result.append(toLetter(addFactor + input))
	}
}

private fun toLetter(value: Int) = 'A' + value