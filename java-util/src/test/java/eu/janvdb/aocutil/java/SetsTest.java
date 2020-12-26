package eu.janvdb.aocutil.java;

import io.vavr.collection.List;
import io.vavr.collection.Stream;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SetsTest {

	@Test
	public void returnsCorrectSets() {
		Stream<List<Integer>> sets = Sets.sets(List.of(1, 2, 3));

		assertThat(sets).hasSize(8);
	}

}