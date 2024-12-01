import java.util.List;
import java.util.stream.Stream;

public class Solution {

	private static final List<Integer> VALUES = Stream.of(
			12, 123, 1234, 12345, 123456, 1234567, 12345678, 123456789,
			23, 234, 2345, 23456, 234567, 2345678, 23456789,
			34, 345, 3456, 34567, 345678, 3456789,
			45, 456, 4567, 45678, 456789,
			56, 567, 5678, 56789,
			67, 678, 6789,
			78, 789,
			89)
			.sorted().toList();

	public List<Integer> sequentialDigits(int low, int high) {
		return VALUES.stream().filter(i -> i >= low && i <= high).toList();
	}

	public static void main(String[] args) {
		System.out.println(new Solution().sequentialDigits(100, 300));
	}

}
