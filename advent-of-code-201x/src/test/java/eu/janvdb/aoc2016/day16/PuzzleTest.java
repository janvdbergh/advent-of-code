package eu.janvdb.aoc2016.day16;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PuzzleTest {

	@Test
	public void correctlyCalculatesReverseDragon() {
		Puzzle puzzle = new Puzzle();
		assertThat(puzzle.reverseDragon("1")).isEqualTo("100");
		assertThat(puzzle.reverseDragon("0")).isEqualTo("001");
		assertThat(puzzle.reverseDragon("11111")).isEqualTo("11111000000");
		assertThat(puzzle.reverseDragon("111100001010")).isEqualTo("1111000010100101011110000");
	}

	@Test
	public void correctlyCalculatesChecksum() {
		Puzzle puzzle = new Puzzle();
		assertThat(puzzle.calculateChecksum("110010110100")).isEqualTo("100");
	}

	@Test
	public void correctlyCalculatesData() {
		Puzzle puzzle = new Puzzle();
		assertThat(puzzle.calculateData(20, "10000")).isEqualTo("1000001111001000011101100");
	}

}