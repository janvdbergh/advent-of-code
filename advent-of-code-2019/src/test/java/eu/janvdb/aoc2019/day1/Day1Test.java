package eu.janvdb.aoc2019.day1;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Day1Test {

	@Test
	public void calculatesFuel1Correctly() {
		assertThat(new Day1().calculateFuel1(14)).isEqualTo(2);
		assertThat(new Day1().calculateFuel1(1969)).isEqualTo(654);
		assertThat(new Day1().calculateFuel1(100756)).isEqualTo(33583);
	}

	@Test
	public void calculatesFuel2Correctly() {
		assertThat(new Day1().calculateFuel2(14)).isEqualTo(2);
		assertThat(new Day1().calculateFuel2(1969)).isEqualTo(966);
		assertThat(new Day1().calculateFuel2(100756)).isEqualTo(50346);
	}
}