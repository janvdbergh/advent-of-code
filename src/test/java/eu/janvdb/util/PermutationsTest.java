package eu.janvdb.util;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import javaslang.collection.List;
import javaslang.collection.Set;

public class PermutationsTest {

	@Test
	public void returnsCorrectPermutations() {
		Set<List<Integer>> result = Permutations.getAllPermutations(List.of(1, 2, 3));

		assertTrue(result.containsAll(List.of(
				List.of(1, 2, 3),
				List.of(1, 3, 2),
				List.of(2, 1, 3),
				List.of(2, 3, 1),
				List.of(3, 1, 2),
				List.of(3, 2, 1)
		)));
	}

}