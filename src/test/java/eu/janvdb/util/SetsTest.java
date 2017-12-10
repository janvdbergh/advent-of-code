package eu.janvdb.util;

import io.vavr.collection.List;
import io.vavr.collection.Stream;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SetsTest {

	@Test
	public void returnsCorrectSets() {
		Stream<List<Integer>> sets = Sets.sets(List.of(1, 2, 3));

		assertEquals(8, sets.size());
	}

}