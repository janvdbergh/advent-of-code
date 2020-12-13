package eu.janvdb.aoc2020.common

fun gcd(a: Long, b: Long): Long {
	var x = a
	var y = b
	var z = x % y
	while (z != 0L) {
		x = y
		y = z
		z = x % y
	}
	return y
}
