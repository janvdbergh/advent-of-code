package eu.janvdb.aoc2017.day10;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class KnotterTest {

	@Test
	public void returnsCorrectValues() {
		Knotter knotter = new Knotter();

		assertThat(knotter.knotString("")).isEqualTo("a2582a3a0e66e6e86e3812dcb672a272");
		assertThat(knotter.knotString("AoC 2017")).isEqualTo("33efeb34ea91902bb2f59c9920caa6cd");
		assertThat(knotter.knotString("1,2,3")).isEqualTo("3efbe78a8d82f29979031a4aa0b16a9d");
		assertThat(knotter.knotString("1,2,4")).isEqualTo("63960835bcdc130f0b66d7ff4f6a5a8e");
	}

}