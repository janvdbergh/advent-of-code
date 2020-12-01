package eu.janvdb.aoc2017.day3;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PositionTest {

	@Test
	public void returnsCorrectPositions() {
		Position position1024 = Position.at(1024);
		System.out.println(position1024);

		assertThat(position1024.getDistance()).isEqualTo(31);
	}
}