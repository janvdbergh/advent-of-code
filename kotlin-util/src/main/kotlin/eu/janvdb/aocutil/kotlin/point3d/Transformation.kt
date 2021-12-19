package eu.janvdb.aocutil.kotlin.point3d

class Transformation private constructor(private val matrix: Array<IntArray>) {
	operator fun times(point: Point3D): Point3D {
		return Point3D(
			point.x * matrix[0][0] + point.y * matrix[0][1] + point.z * matrix[0][2],
			point.x * matrix[1][0] + point.y * matrix[1][1] + point.z * matrix[1][2],
			point.x * matrix[2][0] + point.y * matrix[2][1] + point.z * matrix[2][2],
		)
	}

	operator fun times(other: Transformation): Transformation {
		val newMatrix = arrayOf(intArrayOf(0, 0, 0), intArrayOf(0, 0, 0), intArrayOf(0, 0, 0))
		for (i in 0 until 3) {
			for (j in 0 until 3) {
				for (k in 0 until 3) {
					newMatrix[i][j] += this.matrix[i][k] * other.matrix[k][j]
				}
			}
		}
		return Transformation(newMatrix)
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false
		other as Transformation
		if (!matrix.contentDeepEquals(other.matrix)) return false
		return true
	}

	override fun hashCode() = matrix.contentDeepHashCode()

	override fun toString(): String {
		val result = StringBuilder("[")
		for (i in 0 until 3) {
			if (i != 0) result.append(", ")
			result.append("[")
			for (j in 0 until 3) {
				if (j != 0) result.append(", ")
				result.append(matrix[i][j])
			}
			result.append("]")
		}
		result.append("]")
		return result.toString()
	}

	companion object {
		val UNITY = Transformation(arrayOf(intArrayOf(1, 0, 0), intArrayOf(0, 1, 0), intArrayOf(0, 0, 1)))
		val ROTATION_X = Transformation(arrayOf(intArrayOf(1, 0, 0), intArrayOf(0, 0, -1), intArrayOf(0, 1, 0)))
		val ROTATION_Y = Transformation(arrayOf(intArrayOf(0, 0, 1), intArrayOf(0, 1, 0), intArrayOf(-1, 0, 0)))
		val ROTATION_Z = Transformation(arrayOf(intArrayOf(0, -1, 0), intArrayOf(1, 0, 0), intArrayOf(0, 0, 1)))

		private val ROTATIONS_X = setOf(UNITY, ROTATION_X, ROTATION_X * ROTATION_X, ROTATION_X * ROTATION_X * ROTATION_X)
		private val ROTATIONS_Y = setOf(UNITY, ROTATION_Y, ROTATION_Y * ROTATION_Y, ROTATION_Y * ROTATION_Y * ROTATION_Y)
		private val ROTATIONS_Z = setOf(UNITY, ROTATION_Z, ROTATION_Z * ROTATION_Z, ROTATION_Z * ROTATION_Z * ROTATION_Z)
		val ALL_ROTATIONS = ROTATIONS_X
			.flatMap { x -> ROTATIONS_Y.map { y -> x * y } }
			.flatMap { xy -> ROTATIONS_Z.map { z -> xy * z } }
			.toSet()
	}
}