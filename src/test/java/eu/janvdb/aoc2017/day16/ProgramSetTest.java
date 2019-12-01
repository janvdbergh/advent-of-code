package eu.janvdb.aoc2017.day16;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ProgramSetTest {

	@Test
	public void testSpin() {
		ProgramSet programSet = new ProgramSet(5)
				.spin(3);
		assertThat(programSet.getProgramList()).isEqualTo("cdeab");
	}

	@Test
	public void testExchange() {
		ProgramSet programSet = new ProgramSet(5)
				.exchange(1, 3);
		assertThat(programSet.getProgramList()).isEqualTo("adcbe");
	}

	@Test
	public void testPartner() {
		ProgramSet programSet = new ProgramSet(5)
				.partner('b', 'e');
		assertThat(programSet.getProgramList()).isEqualTo("aecdb");
	}

	@Test
	public void testWithExample() {
		ProgramSet programSet = new ProgramSet(5)
				.spin(1)
				.exchange(3, 4)
				.partner('e', 'b');
		assertThat(programSet.getProgramList()).isEqualTo("baedc");
	}
}