package eu.janvdb.aoc2018.day4;

import eu.janvdb.aocutil.java.FileReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day4 {

	private static final String DATE_PATTERN = "\\[\\d\\d\\d\\d-\\d\\d-\\d\\d \\d\\d:(\\d\\d)]";
	private static final Pattern GUARD_LINE = Pattern.compile(DATE_PATTERN + " Guard #(\\d+) begins shift");
	private static final Pattern SLEEPS_LINE = Pattern.compile(DATE_PATTERN + " falls asleep");
	private static final Pattern WAKES_LINE = Pattern.compile(DATE_PATTERN + " wakes up");

	public static void main(String[] args) throws IOException {
		List<SleepPeriod> sleepPeriods = readData();

		List<GuardWithSleepPeriods> guardWithSleepPeriods = sleepPeriods.stream()
				.map(SleepPeriod::getGuard)
				.map(guard -> new GuardWithSleepPeriods(guard, sleepPeriods.stream().filter(sleepPeriod -> sleepPeriod.getGuard() == guard).collect(Collectors.toList())))
				.collect(Collectors.toList());

		GuardWithSleepPeriods mostSleepingGuard = guardWithSleepPeriods.stream()
				.max(Comparator.comparing(GuardWithSleepPeriods::getTotalSleepTime))
				.orElseThrow();
		System.out.println(mostSleepingGuard);

		GuardWithSleepPeriods guardMostAsleepAtCertainMinute = guardWithSleepPeriods.stream()
				.max(Comparator.comparing(x -> x.getMostSleptMinute().getTimesSlept()))
				.orElseThrow();
		System.out.println(guardMostAsleepAtCertainMinute);
	}

	private static List<SleepPeriod> readData() {
		List<SleepPeriod> result = new ArrayList<>();
		Integer currentGuard = null;
		Integer sleepStart = null;

		List<String> lines = FileReader.readStringFile(Day4.class, "day4.txt");
		lines.sort(String::compareTo);
		for (String line : lines) {
			Matcher guardLineMatcher = GUARD_LINE.matcher(line);
			if (guardLineMatcher.matches()) {
				if (sleepStart != null) {
					throw new IllegalStateException("Guard did not wake");
				}

				currentGuard = Integer.parseInt(guardLineMatcher.group(2));
				continue;
			}

			Matcher sleepsMatcher = SLEEPS_LINE.matcher(line);
			if (sleepsMatcher.matches()) {
				sleepStart =  Integer.parseInt(sleepsMatcher.group(1));
				continue;
			}

			Matcher wakesMatcher = WAKES_LINE.matcher(line);
			if (wakesMatcher.matches()) {
				if (currentGuard == null) {
					throw new IllegalStateException("No guard line found");
				}
				if (sleepStart == null) {
					throw new IllegalStateException("Sleep not started");
				}

				int sleepEnd = Integer.parseInt(wakesMatcher.group(1));
				result.add(new SleepPeriod(currentGuard, sleepStart, sleepEnd));

				sleepStart = null;
				continue;
			}

			throw new IllegalArgumentException("Invalid line: " + line);
		}

		if (sleepStart != null) {
			throw new IllegalStateException("Guard did not wake");
		}

		return result;
	}
}

