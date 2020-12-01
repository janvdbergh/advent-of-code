package eu.janvdb.aoc2018.day8;

import eu.janvdb.aoc2018.util.FileReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day8 {

	public static void main(String[] args) throws IOException {
		List<Integer> input = FileReader.readIntFile(Day8.class, "day8.txt");

		Node root = readNode(input, 0);
		System.out.println(root.size());
		System.out.println(root.checkSum());
		System.out.println(root.value());
	}

	private static Node readNode(List<Integer> input, int start) {
		int numberOfChildren = input.get(start);
		int numberOfMetadata = input.get(start + 1);
		start += 2;

		List<Node> children = new ArrayList<>();
		for (int i = 0; i < numberOfChildren; i++) {
			Node child = readNode(input, start);
			children.add(child);
			start += child.size();
		}

		List<Integer> metadata = new ArrayList<>();
		for (int i = 0; i < numberOfMetadata; i++) {
			metadata.add(input.get(start));
			start++;
		}

		return new Node(children, metadata);
	}

}

