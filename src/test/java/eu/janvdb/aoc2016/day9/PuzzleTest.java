package eu.janvdb.aoc2016.day9;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PuzzleTest {

	@Test
	public void correctlyDecompresses() {
		Puzzle puzzle = new Puzzle();

		assertThat(puzzle.getDecompressedLength1("ADVENT")).isEqualTo(6);
		assertThat(puzzle.getDecompressedLength1("A(1x5)BC")).isEqualTo(7);
		assertThat(puzzle.getDecompressedLength1("(3x3)XYZ")).isEqualTo(9);
		assertThat( puzzle.getDecompressedLength1("A(2x2)BCD(2x2)EFG")).isEqualTo(11);
		assertThat(puzzle.getDecompressedLength1("(6x1)(1x3)A")).isEqualTo(6);
		assertThat( puzzle.getDecompressedLength1("X(8x2)(3x3)ABCY")).isEqualTo(18);
	}

	@Test
	public void correctlyDecompresses2() {
		Puzzle puzzle = new Puzzle();
		assertThat(puzzle.getDecompressedLength2("ADVENT")).isEqualTo(6);
		assertThat(puzzle.getDecompressedLength2("(3x3)XYZ")).isEqualTo(9);
		assertThat(puzzle.getDecompressedLength2("(27x12)(20x12)(13x14)(7x10)(1x12)A")).isEqualTo(241920);
		assertThat(puzzle.getDecompressedLength2("(25x3)(3x3)ABC(2x3)XY(5x2)PQRSTX(18x9)(3x2)TWO(5x7)SEVEN")).isEqualTo(445);
	}
}