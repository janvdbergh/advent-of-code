package eu.janvdb.aoc2016.day10;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Puzzle {

	private static final Pattern INIT_PATTERN = Pattern.compile("value (\\d+) goes to bot (\\d+)");
	private static final Pattern MOVE_PATTERN = Pattern.compile("bot (\\d+) gives low to (bot|output) (\\d+) and high to (bot|output) (\\d+)");

	private Map<Integer, Bot> botMap = new HashMap<>();
	private Map<Integer, OutputBin> outputBinMap = new HashMap<>();

	public static void main(String[] args) throws IOException {
		new Puzzle().execute();
	}

	private void execute() throws IOException {
		FileUtils.readLines(new File(getClass().getResource("input.txt").getFile()), "UTF-8")
				.forEach(this::parse);

		Collection<Bot> botsToMove = botMap.values();
		while (!botsToMove.isEmpty()) {
			botsToMove = botsToMove.stream()
					.filter(bot -> !bot.execute())
					.collect(Collectors.toSet());
		}

		botMap.values().stream()
				.filter(bot -> bot.getValues().containsAll(Arrays.asList(17, 61)))
				.map(Receiver::getNumber)
				.forEach(System.out::println);

		int product = IntStream.range(0, 3)
				.mapToObj(outputBinMap::get)
				.map(OutputBin::getValues)
				.mapToInt(list -> list.get(0))
				.reduce(1, (x, y) -> x * y);
		System.out.println(product);
	}

	private void parse(String line) {
		Matcher inputPatternMatcher = INIT_PATTERN.matcher(line);
		if (inputPatternMatcher.matches()) {
			int value = Integer.parseInt(inputPatternMatcher.group(1));
			int botNumber = Integer.parseInt(inputPatternMatcher.group(2));
			getBot(botNumber).receive(value);

			return;
		}

		Matcher movePatternMatcher = MOVE_PATTERN.matcher(line);
		if (movePatternMatcher.matches()) {
			int botNumber = Integer.parseInt(movePatternMatcher.group(1));
			String lowReceiverType = movePatternMatcher.group(2);
			int lowReceiver = Integer.parseInt(movePatternMatcher.group(3));
			String highReceiverType = movePatternMatcher.group(4);
			int highReceiver = Integer.parseInt(movePatternMatcher.group(5));

			getBot(botNumber).setInstruction(
					new Instruction(
							getReceiver(lowReceiverType, lowReceiver),
							getReceiver(highReceiverType, highReceiver)
					)
			);
			return;
		}

		throw new IllegalArgumentException(line);
	}

	private Receiver getReceiver(String receiverType, int receiver) {
		switch (receiverType) {
			case "bot":
				return getBot(receiver);
			case "output":
				return getBin(receiver);
			default:
				throw new IllegalArgumentException(receiverType);
		}
	}

	private Bot getBot(int botNumber) {
		if (!botMap.containsKey(botNumber)) {
			botMap.put(botNumber, new Bot(botNumber));
		}
		return botMap.get(botNumber);
	}

	private OutputBin getBin(int binNumber) {
		if (!outputBinMap.containsKey(binNumber)) {
			outputBinMap.put(binNumber, new OutputBin(binNumber));
		}
		return outputBinMap.get(binNumber);
	}
}
