package eu.janvdb.aoc2016.day20;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BlockCanBeCombinedTest {

	@Test
	public void correctlyReturnsOverlaps() {
		assertThat(block(2, 5).canBeCombined(block(5, 10))).isTrue();
		assertThat(block(2, 5).canBeCombined(block(1, 2))).isTrue();
		assertThat(block(2, 5).canBeCombined(block(3, 10))).isTrue();
		assertThat(block(2, 5).canBeCombined(block(3, 4))).isTrue();
		assertThat(block(2, 5).canBeCombined(block(6, 10))).isTrue();
		assertThat(block(2, 5).canBeCombined(block(7, 10))).isFalse();

		assertThat(block(5, 10).canBeCombined(block(2, 5))).isTrue();
		assertThat(block(1, 2).canBeCombined(block(2, 5))).isTrue();
		assertThat(block(3, 10).canBeCombined(block(2, 5))).isTrue();
		assertThat(block(3, 4).canBeCombined(block(2, 5))).isTrue();
		assertThat(block(6, 10).canBeCombined(block(2, 5))).isTrue();
		assertThat(block(7, 10).canBeCombined(block(2, 5))).isFalse();
	}

	private Block block(long from, long to) {
		return new Block(from, to);
	}

}