package eu.janvdb.aocutil.java;

import io.vavr.collection.List;
import io.vavr.collection.Stream;

public class Sets {

	public static <T> Stream<List<T>> sets(List<T> items) {
		assert (items.size() <= 31);

		return items.toStream().combinations()
				.map(Stream::toList);
	}


}
