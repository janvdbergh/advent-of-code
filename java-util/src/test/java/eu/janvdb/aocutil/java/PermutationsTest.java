package eu.janvdb.aocutil.java;

import io.vavr.collection.List;
import io.vavr.collection.Stream;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PermutationsTest {

	@Test
	public void returnsCorrectPermutations() {
		Stream<List<Integer>> result = Permutations.getAllPermutations(List.of(1, 2, 3));

		assertThat(result.containsAll(List.of(
				List.of(1, 2, 3),
				List.of(1, 3, 2),
				List.of(2, 1, 3),
				List.of(2, 3, 1),
				List.of(3, 1, 2),
				List.of(3, 2, 1)
		))).isTrue();
	}

}