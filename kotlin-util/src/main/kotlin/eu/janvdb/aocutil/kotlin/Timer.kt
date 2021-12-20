package eu.janvdb.aocutil.kotlin

fun <T> runWithTimer(name: String, action: () -> T): T {
	val startTime = System.currentTimeMillis()
	val result = action()
	val endTime = System.currentTimeMillis()
	println("$name ran in ${endTime - startTime} ms.")

	return result
}