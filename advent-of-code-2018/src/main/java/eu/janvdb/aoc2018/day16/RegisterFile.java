package eu.janvdb.aoc2018.day16;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterFile {

	private final static int NUMBER_OF_REGISTERS = 6;
	private final static Pattern REGISTER_FILE_PATTERN = Pattern.compile("(\\d+),\\s*(\\d+),\\s*(\\d+),\\s*(\\d+)");

	private final long[] values;

	RegisterFile(String description) {
		Matcher matcher = REGISTER_FILE_PATTERN.matcher(description);
		if (!matcher.find()) throw new IllegalArgumentException(description);

		values = new long[NUMBER_OF_REGISTERS];
		for(int i=0; i<NUMBER_OF_REGISTERS; i++) {
			values[i] = Integer.parseInt(matcher.group(i+1));
		}
	}

	public RegisterFile() {
		values = new long[NUMBER_OF_REGISTERS];
	}

	public void set(int register, long value) {
		values[register] = value;
	}

	public long get(int register) {
		if (register < 0 || register > NUMBER_OF_REGISTERS) return Long.MAX_VALUE;
		return values[register];
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		RegisterFile that = (RegisterFile) o;
		return Arrays.equals(values, that.values);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(values);
	}

	@Override
	public String toString() {
		return "RegisterFile{" +
				"values=" + Arrays.toString(values) +
				'}';
	}
}
