package eu.janvdb.aoc2018.day17;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import eu.janvdb.aoc2018.util.FileReader;

public class Day17 {

	public static void main(String[] args) throws IOException {
		List<Block> blocks = FileReader.readStringFile(Day17.class, "day17.txt").stream()
				.map(Block::new)
				.collect(Collectors.toList());

		Map map = new Map(blocks);
		map.fill();
		map.print();
	}
}

