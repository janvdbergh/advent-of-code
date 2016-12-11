package eu.janvdb.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Histogram {

	public static <T extends Comparable<T>> List<HistogramEntry<T>> createHistogram(Collection<T> items) {
		Map<T, Integer> histogram = items.stream()
				.collect(Collectors.groupingBy(
						Function.identity(),
						Collectors.mapping(t -> 1, Collectors.summingInt(s -> s))
				));

		return histogram.keySet().stream()
				.map(t -> new HistogramEntry<>(t, histogram.get(t)))
				.sorted()
				.collect(Collectors.toList());
	}

	public static class HistogramEntry<T extends Comparable<T>> implements Comparable<HistogramEntry<T>> {
		private final T item;
		private final int count;

		public HistogramEntry(T item, int count) {
			this.item = item;
			this.count = count;
		}

		@Override
		public int compareTo(HistogramEntry<T> o) {
			int countDiff = o.count - count;
			return countDiff != 0 ? countDiff : item.compareTo(o.item);
		}

		public T getItem() {
			return item;
		}

		public int getCount() {
			return count;
		}
	}

}
