package eu.janvdb.aoc2020.common

import java.math.BigInteger

fun plusModN(a: BigInteger, b: BigInteger, n: BigInteger): BigInteger {
	val x = (a + b) % n
	return if (x >= BigInteger.ZERO) x else n + x
}

fun minusModN(a: BigInteger, b: BigInteger, n: BigInteger): BigInteger {
	return plusModN(a, -b, n)
}

/**
 * Equation of the form x = a (mod n)
 */
data class ChineseRemainderTheoremEquation(val a: BigInteger, val n: BigInteger)

fun solveChineseRemainderTheorem(equations: List<ChineseRemainderTheoremEquation>): BigInteger {
	val N = equations.foldRight(BigInteger.ONE, { equation, acc -> acc * equation.n })

	var X = BigInteger.ZERO
	for (equation in equations) {
		val y = N / equation.n
		val z = y.modInverse(equation.n)
		val x = equation.a * y * z
		X += x
	}

	return X % N
}

