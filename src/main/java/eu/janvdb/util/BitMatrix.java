package eu.janvdb.util;

public interface BitMatrix {

	static BitMatrix create(int width, int height) {
		return new FullBitMatrix(width, height);
	}

	static String toString(BitMatrix bitMatrix) {
		StringBuilder builder = new StringBuilder();

		for (int y = 0; y < bitMatrix.getHeight(); y++) {
			if (y != 0) builder.append('\n');
			for (int x = 0; x < bitMatrix.getWidth(); x++) {
				builder.append(bitMatrix.getPixel(x, y) ? '#' : '.');
			}
		}
		return builder.toString();
	}

	static boolean equals(BitMatrix bitMatrix, Object o) {
		if (bitMatrix == o) return true;
		if (!(o instanceof BitMatrix)) return false;

		BitMatrix bitMatrix2 = (BitMatrix) o;
		if (bitMatrix.getWidth() != bitMatrix2.getWidth() || bitMatrix.getHeight() != bitMatrix2.getHeight()) {
			return false;
		}

		for (int x = 0; x < bitMatrix.getWidth(); x++) {
			for (int y = 0; y < bitMatrix.getHeight(); y++) {
				if (bitMatrix.getPixel(x, y) != bitMatrix2.getPixel(x, y)) return false;
			}
		}

		return true;
	}

	int getWidth();

	int getHeight();

	boolean getPixel(int x, int y);

	void setPixel(int x, int y, boolean value);

	BitMatrix getSubMatrix(int left, int top, int width, int height);

	default void fill(String values) {
		if (values.length() != getWidth() * getHeight()) throw new IllegalArgumentException();

		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				setPixel(x, y, values.charAt(x + y * getWidth()) == '#');
			}
		}
	}

	default void fill(BitMatrix bitMatrix) {
		if (bitMatrix.getWidth() != getWidth() && bitMatrix.getHeight() != getHeight())
			throw new IllegalArgumentException();

		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				setPixel(x, y, bitMatrix.getPixel(x, y));
			}
		}
	}

	default BitMatrix rotated90() {
		BitMatrix result = create(getHeight(), getWidth());
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				result.setPixel(getHeight() - y - 1, x, getPixel(x, y));
			}
		}

		return result;
	}

	default BitMatrix rotated180() {
		return rotated90().rotated90();
	}

	default BitMatrix rotated270() {
		return rotated180().rotated90();
	}

	default BitMatrix flipHorizontal() {
		BitMatrix result = create(getWidth(), getHeight());
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				result.setPixel(x, y, getPixel(getWidth() - x - 1, y));
			}
		}

		return result;
	}

	default BitMatrix flipVertical() {
		return flipHorizontal().rotated180();
	}

	default int countPixels() {
		int sum = 0;
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				if (getPixel(x, y)) sum++;
			}
		}
		return sum;
	}
}
