package eu.janvdb.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Sets {

	public static <T> Stream<List<T>> sets(List<T> items) {
		assert (items.size() <= 31);

		return IntStream.range(0, (1 << items.size()) - 1).parallel()
				.mapToObj(index -> mapToItems(index, items));
	}

	private static <T> List<T> mapToItems(int index, List<T> items) {
		List<T> result = new ArrayList<>();
		for (int i = 0; i < items.size(); i++) {
			if ((index & (1 << i)) != 0) {
				result.add(items.get(i));
			}
		}

		return result;
	}


}
