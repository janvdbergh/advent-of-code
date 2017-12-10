package eu.janvdb.aoc2017.day10;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class KnotTest {

	@Test
	public void shufflesCorrectly()  {
		Knot knot = new Knot(5);

		knot = knot.doStep(3).doStep(4).doStep(1).doStep(5);

		assertThat(knot.getNumbers()).containsExactly(3, 4, 2, 1, 0);
	}

}