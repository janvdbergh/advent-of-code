package eu.janvdb.aoc2016.day9;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PuzzleTest {

	@Test
	public void correctlyDecompresses() {
		Puzzle puzzle = new Puzzle();

		assertEquals(6, puzzle.getDecompressedLength1("ADVENT"));
		assertEquals(7, puzzle.getDecompressedLength1("A(1x5)BC"));
		assertEquals(9, puzzle.getDecompressedLength1("(3x3)XYZ"));
		assertEquals(11, puzzle.getDecompressedLength1("A(2x2)BCD(2x2)EFG"));
		assertEquals(6, puzzle.getDecompressedLength1("(6x1)(1x3)A"));
		assertEquals(18, puzzle.getDecompressedLength1("X(8x2)(3x3)ABCY"));
	}

	@Test
	public void correctlyDecompresses2() {
		Puzzle puzzle = new Puzzle();

		assertEquals(puzzle.getDecompressedLength2("ADVENT"), 6);
		assertEquals(puzzle.getDecompressedLength2("(3x3)XYZ"), 9);
		assertEquals(puzzle.getDecompressedLength2("(27x12)(20x12)(13x14)(7x10)(1x12)A"), 241920);
		assertEquals(puzzle.getDecompressedLength2("(25x3)(3x3)ABC(2x3)XY(5x2)PQRSTX(18x9)(3x2)TWO(5x7)SEVEN"), 445);
	}
}