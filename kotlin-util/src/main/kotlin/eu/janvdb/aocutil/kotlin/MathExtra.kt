package eu.janvdb.aocutil.kotlin

fun gcd(a: Int, b: Int): Int {
	if (b == 0) return a
	return gcd(b, a % b)
}

fun gcd(a: Long, b: Long): Long {
	if (b == 0L) return a
	return gcd(b, a % b)
}

fun lcm(a: Long, b: Long): Long {
	return a / gcd(a, b) * b
}
