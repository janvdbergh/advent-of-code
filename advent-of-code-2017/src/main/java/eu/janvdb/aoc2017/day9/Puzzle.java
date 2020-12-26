package eu.janvdb.aoc2017.day9;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class Puzzle {

	public static void main(String[] args) throws IOException {
		File file = new File(Puzzle.class.getResource("input.txt").getFile());
		String input = FileUtils.readFileToString(file, "UTF-8");

		BlockParser blockParser = new BlockParser();
		System.out.println(blockParser.countGroups(input));
		System.out.println(blockParser.countGarbage(input));
	}
}
