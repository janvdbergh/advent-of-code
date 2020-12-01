package eu.janvdb.aoc2018.day13;

enum Turn {
	LEFT, STRAIGHT, RIGHT;

	Turn next() {
		Turn[] values = Turn.values();
		int index = (this.ordinal() + 1) % values.length;
		return values[index];
	}
}
