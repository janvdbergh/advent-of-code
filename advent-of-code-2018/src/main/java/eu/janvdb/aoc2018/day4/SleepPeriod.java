package eu.janvdb.aoc2018.day4;

class SleepPeriod {

	private final int guard;
	private final int startMinute;
	private final int endMinute;

	SleepPeriod(int guard, int startMinute, int endMinute) {
		this.guard = guard;
		this.startMinute = startMinute;
		this.endMinute = endMinute;
	}

	int getGuard() {
		return guard;
	}

	int getSleepDuration() {
		return endMinute - startMinute;
	}

	@Override
	public String toString() {
		return "SleepPeriod{" +
				"guard=" + guard +
				", start=" + startMinute +
				", end=" + endMinute +
				'}';
	}

	boolean contains(int minute) {
		return startMinute <= minute && minute < endMinute;
	}
}
