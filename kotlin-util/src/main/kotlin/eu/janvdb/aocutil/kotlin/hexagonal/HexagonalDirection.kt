package eu.janvdb.aocutil.kotlin.hexagonal

enum class HexagonalDirection(val coordinate: HexagonalCoordinate) {
	E(HexagonalCoordinate(2, 0)),
	SE(HexagonalCoordinate(1, 1)),
	SW(HexagonalCoordinate(-1, 1)),
	W(HexagonalCoordinate(-2, 0)),
	NW(HexagonalCoordinate(-1, -1)),
	NE(HexagonalCoordinate(1, -1));
}