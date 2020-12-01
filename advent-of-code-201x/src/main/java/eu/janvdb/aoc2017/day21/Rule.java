package eu.janvdb.aoc2017.day21;

import eu.janvdb.util.BitMatrix;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Rule {

	private static final Pattern PATTERN = Pattern.compile("([./#]+) => ([./#]+)");

	private final BitMatrix input;
	private final Set<BitMatrix> inputs;
	private final BitMatrix output;

	Rule(String description) {
		Matcher matcher = PATTERN.matcher(description);
		if (!matcher.matches()) throw new IllegalArgumentException(description);

		String inputStr = matcher.group(1).replaceAll("/", "");
		String outputStr = matcher.group(2).replaceAll("/", "");
		int size = inputStr.length() == 4 ? 2 : 3;

		input = BitMatrix.create(size, size);
		input.fill(inputStr);
		inputs = HashSet.of(input,
				input.rotated90(), input.rotated180(), input.rotated270(),
				input.flipHorizontal(), input.flipVertical(),
				input.rotated90().flipHorizontal(), input.rotated90().flipVertical()
		);

		output = BitMatrix.create(size + 1, size + 1);
		output.fill(outputStr);
	}

	boolean matches(Grid grid, int x, int y) {
		BitMatrix gridSubMatrix = grid.getBitMatrix().getSubMatrix(x, y, getInputSize(), getInputSize());
		return inputs.contains(gridSubMatrix);
	}

	int getInputSize() {
		return input.getWidth();
	}

	BitMatrix getOutput() {
		return output;
	}

	@Override
	public String toString() {
		return input.toString() + "\n  =>\n" + output.toString();
	}
}
