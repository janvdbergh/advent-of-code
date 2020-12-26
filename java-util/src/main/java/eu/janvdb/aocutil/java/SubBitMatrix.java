package eu.janvdb.aocutil.java;

import java.util.Objects;

public class SubBitMatrix implements BitMatrix {

	private final BitMatrix original;
	private final int width, height, left, top;

	SubBitMatrix(BitMatrix original, int left, int top, int width, int height) {
		this.original = original;
		this.width = width;
		this.height = height;
		this.left = left;
		this.top = top;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public boolean getPixel(int x, int y) {
		return original.getPixel(x + left, y + top);
	}

	@Override
	public void setPixel(int x, int y, boolean value) {
		original.setPixel(x + left, y + top, value);
	}

	@Override
	public BitMatrix getSubMatrix(int left, int top, int width, int height) {
		return new SubBitMatrix(original, this.left + left, this.top + top, width, height);
	}

	@Override
	public String toString() {
		return BitMatrix.toString(this);
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof SubBitMatrix)) return false;
		return BitMatrix.equals(this, o);
	}

	@Override
	public int hashCode() {
		return Objects.hash(width, height);
	}

}
