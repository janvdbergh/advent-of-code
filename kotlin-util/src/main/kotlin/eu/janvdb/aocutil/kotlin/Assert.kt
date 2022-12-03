package eu.janvdb.aocutil.kotlin

fun assertTrue(value: Boolean, message: String = "") {
	if (!value) {
		throw AssertionError("Assertion failed! " + message)
	}
}