package eu.janvdb.util;

import javaslang.collection.List;
import javaslang.collection.Stream;

public class Sets {

	public static <T> Stream<List<T>> sets(List<T> items) {
		assert (items.size() <= 31);

		return items.toStream().combinations()
				.map(Stream::toList);
	}


}
