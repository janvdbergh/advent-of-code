package eu.janvdb.aoc2016.day20;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BlockCanBeCombinedTest {

	@Test
	public void correctlyReturnsOverlaps() {
		assertTrue(block(2, 5).canBeCombined(block(5, 10)));
		assertTrue(block(2, 5).canBeCombined(block(1, 2)));
		assertTrue(block(2, 5).canBeCombined(block(3, 10)));
		assertTrue(block(2, 5).canBeCombined(block(3, 4)));
		assertTrue(block(2, 5).canBeCombined(block(6, 10)));
		assertFalse(block(2, 5).canBeCombined(block(7, 10)));

		assertTrue(block(5, 10).canBeCombined(block(2, 5)));
		assertTrue(block(1, 2).canBeCombined(block(2, 5)));
		assertTrue(block(3, 10).canBeCombined(block(2, 5)));
		assertTrue(block(3, 4).canBeCombined(block(2, 5)));
		assertTrue(block(6, 10).canBeCombined(block(2, 5)));
		assertFalse(block(7, 10).canBeCombined(block(2, 5)));
	}

	private Block block(long from, long to) {
		return new Block(from, to);
	}

}