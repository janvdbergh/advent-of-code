package eu.janvdb.aoc2016.day16;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PuzzleTest {

	@Test
	public void correctlyCalculatesReverseDragon() {
		Puzzle puzzle = new Puzzle();
		assertEquals("100", puzzle.reverseDragon("1"));
		assertEquals("001", puzzle.reverseDragon("0"));
		assertEquals("11111000000", puzzle.reverseDragon("11111"));
		assertEquals("1111000010100101011110000", puzzle.reverseDragon("111100001010"));
	}

	@Test
	public void correctlyCalculatesChecksum() {
		Puzzle puzzle = new Puzzle();
		assertEquals("100", puzzle.calculateChecksum("110010110100"));
	}

	@Test
	public void correctlyCalculatesData() {
		Puzzle puzzle = new Puzzle();
		assertEquals("1000001111001000011101100", puzzle.calculateData(20, "10000"));
	}

}