package eu.janvdb.aoc2018.day1;

import eu.janvdb.aoc2018.util.Holder;
import eu.janvdb.aocutil.java.FileReader;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class Day1 {

	public static void main(String[] args) throws IOException {
		List<String> lines = FileReader.readStringFile(Day1.class, "day1.txt");
		long result1 = lines.stream()
				.mapToLong(Long::parseLong)
				.sum();
		System.out.println(result1);

		Holder<Long> runningSum = new Holder<>(0L);

		Set<Long> foundItems = new HashSet<>();
		foundItems.add(0L);

		long result2 = Stream.generate(() -> lines)
				.flatMap(Collection::stream)
				.mapToLong(Long::parseLong)
				.map(value -> {
					Long sum = runningSum.get() + value;
					runningSum.set(sum);
					return sum;
				})
				.filter(sum -> {
					System.out.println(sum);
					boolean result = foundItems.contains(sum);
					foundItems.add(sum);
					return result;
				})
				.findFirst()
				.orElseThrow(IllegalStateException::new);
		System.out.println(result2);
	}

}
