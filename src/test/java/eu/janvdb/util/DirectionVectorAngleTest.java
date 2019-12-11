package eu.janvdb.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;

class DirectionVectorAngleTest {

	@Test
	void returnsCorrectAngles() {
		assertThat(new DirectionVector(0, -1).getAngleInDegreesWithZeroOnTop()).isCloseTo(0, offset(0.1));
		assertThat(new DirectionVector(1, 0).getAngleInDegreesWithZeroOnTop()).isCloseTo(90, offset(0.1));
		assertThat(new DirectionVector(0, 1).getAngleInDegreesWithZeroOnTop()).isCloseTo(180, offset(0.1));
		assertThat(new DirectionVector(-1, 0).getAngleInDegreesWithZeroOnTop()).isCloseTo(270, offset(0.1));
	}
}