package eu.janvdb.aoc2018.day8;

import java.util.List;

class Node {

	private final List<Node> children;
	private final List<Integer> metaData;

	Node(List<Node> children, List<Integer> metaData) {
		this.children = children;
		this.metaData = metaData;
	}

	int size() {
		return 2 + children.stream().mapToInt(Node::size).sum() + metaData.size();
	}

	int checkSum() {
		return
				children.stream().mapToInt(Node::checkSum).sum() +
						metaData.stream().mapToInt(x -> x).sum();
	}

	int value() {
		if (children.size() == 0) {
			return metaData.stream().mapToInt(x -> x).sum();
		}

		return metaData.stream()
				.mapToInt(x -> x - 1)
				.filter(x -> x>=0 && x<children.size())
				.map(x -> children.get(x).value())
				.sum();
	}
}
