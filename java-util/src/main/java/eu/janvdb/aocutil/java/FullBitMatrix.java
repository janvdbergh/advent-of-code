package eu.janvdb.aocutil.java;

import java.util.Objects;

public class FullBitMatrix implements BitMatrix {

	private final int width, height;
	private final boolean[] pixels;

	FullBitMatrix(int width, int height) {
		this.width = width;
		this.height = height;
		this.pixels = new boolean[width * height];
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
		return pixels[getIndex(x, y)];
	}

	@Override
	public void setPixel(int x, int y, boolean value) {
		pixels[getIndex(x, y)] = value;
	}

	@Override
	public BitMatrix getSubMatrix(int left, int top, int width, int height) {
		return new SubBitMatrix(this, left, top, width, height);
	}

	private int getIndex(int x, int y) {
		return x + y * width;
	}

	@Override
	public String toString() {
		return BitMatrix.toString(this);
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof FullBitMatrix)) return false;
		return BitMatrix.equals(this, o);
	}

	@Override
	public int hashCode() {
		return Objects.hash(width, height);
	}
}
