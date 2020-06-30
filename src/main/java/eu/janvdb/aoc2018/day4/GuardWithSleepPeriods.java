package eu.janvdb.aoc2018.day4;

import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

class GuardWithSleepPeriods {

	private final int guard;
	private final List<SleepPeriod> sleepPeriods;

	GuardWithSleepPeriods(int guard, List<SleepPeriod> sleepPeriods) {
		this.guard = guard;
		this.sleepPeriods = sleepPeriods;
	}

	int getTotalSleepTime() {
		return sleepPeriods.stream().mapToInt(SleepPeriod::getSleepDuration).sum();
	}

	TimesSleptAtMinute getMostSleptMinute() {
		return IntStream.range(0, 61)
				.mapToObj(minute -> new TimesSleptAtMinute(minute, getPeriodsAsleepAtMinute(minute)))
				.max(Comparator.comparing(TimesSleptAtMinute::getTimesSlept))
				.orElseThrow();
	}

	private int getPeriodsAsleepAtMinute(int minute) {
		return (int) sleepPeriods.stream()
				.filter(sleepPeriod -> sleepPeriod.contains(minute))
				.count();
	}

	@Override
	public String toString() {
		TimesSleptAtMinute mostSleptMinute = getMostSleptMinute();
		return "GuardWithSleepPeriods{" +
				"guard=" + guard +
				", totalSleepTime=" + getTotalSleepTime() +
				", mostSleptMinute=" + getMostSleptMinute() +
				", result=" + (guard * mostSleptMinute.getMinute()) +
				'}';
	}
}
