package eu.janvdb.aoc2018.day4;

class TimesSleptAtMinute {

	private final int minute, timesSlept;

	TimesSleptAtMinute(int minute, int timesSlept) {
		this.minute = minute;
		this.timesSlept = timesSlept;
	}

	int getMinute() {
		return minute;
	}

	int getTimesSlept() {
		return timesSlept;
	}

	@Override
	public String toString() {
		return "TimesSleptAtMinute{minute=" + minute + ", timesSlept=" + timesSlept + '}';
	}
}
