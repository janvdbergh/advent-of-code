package eu.janvdb.aoc2020.day25

import java.math.BigInteger

val MODULO = BigInteger.valueOf(20201227)
val SUBJECT = BigInteger.valueOf(7)

val CARD_PUBLIC_KEY = BigInteger.valueOf(8335663)
val DOOR_PUBLIC_KEY = BigInteger.valueOf(8614349)

fun main() {
	val loopSizeCard = getLoopSize(CARD_PUBLIC_KEY)
	val encryptionKey = DOOR_PUBLIC_KEY.modPow(loopSizeCard, MODULO)
	println(encryptionKey)
}

fun getLoopSize(publicKey: BigInteger): BigInteger {
	var count = BigInteger.ZERO
	var current = BigInteger.ONE
	while (current != publicKey) {
		current = current * SUBJECT % MODULO
		count++
	}
	return count
}