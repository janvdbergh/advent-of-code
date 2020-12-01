package eu.janvdb.util;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Traversable;

import static io.vavr.Function1.identity;

public class Histogram {

	public static <T extends Comparable<T>> List<HistogramEntry<T>> createHistogram(Traversable<T> items) {
		return items
				.groupBy(identity())
				.map((key, keyItems) -> Tuple.of(key, keyItems.size()))
				.toStream()
				.map(HistogramEntry::new)
				.sorted()
				.toList();
	}

	public static class HistogramEntry<T extends Comparable<T>> implements Comparable<HistogramEntry<T>> {
		private final T item;
		private final int count;

		HistogramEntry(Tuple2<T, Integer> item) {
			this.item = item._1;
			this.count = item._2;
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
