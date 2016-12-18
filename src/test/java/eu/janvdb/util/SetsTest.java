package eu.janvdb.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import javaslang.collection.List;
import javaslang.collection.Stream;

public class SetsTest {

	@Test
	public void returnsCorrectSets() {
		Stream<List<Integer>> sets = Sets.sets(List.of(1, 2, 3));

		assertEquals(8, sets.size());
	}

}