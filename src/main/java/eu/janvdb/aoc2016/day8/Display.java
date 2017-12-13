package eu.janvdb.aoc2016.day8;

class Display {

	private final int width;
	private final int height;
	private final boolean[] pixels;

	public Display(int width, int height) {
		this.width = width;
		this.height = height;
		this.pixels = new boolean[width * height];
	}

	public void execute(Command command) {
		switch (command.getInstruction()) {
			case RECT:
				rectangle(command);
				break;
			case ROTATE_COLUMN:
				rotateColumn(command);
				break;
			case ROTATE_ROW:
				rotateRow(command);
				break;
		}

		System.out.println("After command: " + command);
		System.out.println(this);
	}

	public int countPixels() {
		int count = 0;
		for (boolean pixel : pixels) {
			if (pixel) {
				count++;
			}
		}
		return count;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				result.append(getPixel(x, y) ? '#' : '.');
			}
			result.append('\n');
		}

		return result.toString();
	}

	private void rectangle(Command command) {
		for (int x = 0; x < command.getArg1(); x++) {
			for (int y = 0; y < command.getArg2(); y++) {
				setPixel(x, y, true);
			}
		}
	}

	private void rotateColumn(Command command) {
		int x = command.getArg1();
		for (int i = 0; i < command.getArg2(); i++) {
			boolean temp = getPixel(x, height - 1);
			for (int y = height - 2; y >= 0; y--) {
				setPixel(x, y + 1, getPixel(x, y));
			}
			setPixel(x, 0, temp);
		}
	}

	private void rotateRow(Command command) {
		int y = command.getArg1();
		for (int i = 0; i < command.getArg2(); i++) {
			boolean temp = getPixel(width - 1, y);
			for (int x = width - 2; x >= 0; x--) {
				setPixel(x + 1, y, getPixel(x, y));
			}
			setPixel(0, y, temp);
		}
	}

	private boolean getPixel(int x, int y) {
		return pixels[coordinate(x, y)];
	}

	private void setPixel(int x, int y, boolean value) {
		pixels[coordinate(x, y)] = value;
	}

	private int coordinate(int x, int y) {
		return y * width + x;
	}

}
