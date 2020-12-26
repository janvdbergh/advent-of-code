package eu.janvdb.aoc2017.day21;

import eu.janvdb.aocutil.java.BitMatrix;
import io.vavr.collection.List;

class Grid {

	private final BitMatrix bitMatrix;

	Grid() {
		this.bitMatrix = BitMatrix.create(3, 3);
		this.bitMatrix.fill(".#...####");
	}

	private Grid(BitMatrix bitMatrix) {
		this.bitMatrix = bitMatrix;
	}

	int getSize() {
		return bitMatrix.getWidth();
	}

	BitMatrix getBitMatrix() {
		return bitMatrix;
	}

	Grid update(List<Rule> rules) {
		int size = getSize();
		int step = getSize() % 2 == 0 ? 2 : 3;
		int newSize = getSize() / step * (step + 1);
		int newStep = step + 1;

		BitMatrix resultMatrix = BitMatrix.create(newSize, newSize);

		for (int x = 0; x < size / step; x++) {
			for (int y = 0; y < size / step; y++) {
				int x2 = x * step;
				int y2 = y * step;
				Rule matchingRule = rules.find(rule -> rule.getInputSize() == step && rule.matches(this, x2, y2))
						.getOrElseThrow(IllegalStateException::new);

				resultMatrix.getSubMatrix(x * newStep, y * newStep, newStep, newStep)
						.fill(matchingRule.getOutput());
			}
		}

		return new Grid(resultMatrix);
	}

	@Override
	public String toString() {
		return bitMatrix.toString();
	}

}
