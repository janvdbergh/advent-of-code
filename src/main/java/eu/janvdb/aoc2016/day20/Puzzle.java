package eu.janvdb.aoc2016.day20;

import javaslang.collection.List;
import javaslang.collection.Stream;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class Puzzle {

	public static void main(String[] args) {
		new Puzzle().executeA();
		System.out.println();
		new Puzzle().executeB();
	}

	private void executeA() {
		List<Block> blocks = readInput();

		for (int i=0; i<blocks.size(); i++) {
			while (i+1<blocks.size() && blocks.get(i).canBeCombined(blocks.get(i+1))) {
				Block newBlock = blocks.get(i).combine(blocks.get(i+1));
				blocks = blocks.removeAt(i).removeAt(i).insert(i, newBlock);
			}
		}

		System.out.println(blocks.size());
		System.out.println(blocks);

		long sum = blocks.toStream()
				.map(Block::getSize)
				.sum().longValue();
		System.out.println(sum);
		System.out.println((1L<<32) - sum);
	}

	private void executeB() {
		List<Block> blocks = readInput();

		Stream.range(0, 1L<<32 - 1)
				.find(number -> blocks.find(block -> block.contains(number)).isEmpty())
				.forEach(System.out::println);

		Stream.range(0, 1L<<32 - 1)
				.count(number -> blocks.find(block -> block.contains(number)).isEmpty());
	}

	private List<Block> readInput() {
		try {
			return Stream.ofAll(FileUtils.readLines(new File(getClass().getResource("input.txt").getFile()), "UTF-8"))
					.map(Block::new)
					.sorted()
					.toList();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
