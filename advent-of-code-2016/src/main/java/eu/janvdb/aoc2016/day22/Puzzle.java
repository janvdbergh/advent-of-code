package eu.janvdb.aoc2016.day22;

import eu.janvdb.aocutil.java.InputReader;
import eu.janvdb.aocutil.java.Matrix;
import io.vavr.Tuple;
import io.vavr.collection.List;

import java.io.File;

public class Puzzle {

	public static void main(String[] args) {
		new Puzzle().execute();
	}

	private void execute() {
		List<Node> nodes = InputReader.readInput(new File(getClass().getResource("input.txt").getFile()))
				.map(Node::new)
				.toList();

		execute1(nodes);
		execute2(nodes);
	}

	private void execute1(List<Node> nodes) {
		System.out.println(nodes.size());

		int numbeOfViablePairs = nodes.toStream()
				.map(node1 -> nodes.toStream().count(node1::isViablePairWith))
				.sum().intValue();

		System.out.println(numbeOfViablePairs);
	}

	private void execute2(List<Node> nodes) {
		Matrix<Integer, Node> nodeMatrix = new Matrix<>(Node::empty,
				nodes.toStream().map((node) -> Tuple.of(node.getLocation().getX(), node.getLocation().getY(), node))
		);

		int maxX = nodes.map(Node::getLocation).map(Location::getX).max().getOrElseThrow(IllegalStateException::new);
		int maxY = nodes.map(Node::getLocation).map(Location::getY).max().getOrElseThrow(IllegalStateException::new);

		for (int i = 0; i <= maxY; i++) {
			for (int j = 0; j <= maxX; j++) {
				System.out.print(nodeMatrix.get(j, i).toDisplayString());
			}
			System.out.println();
		}
	}

}
