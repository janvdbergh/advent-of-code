package eu.janvdb.aoc2017.day19;


import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;

class Maze {

	private final Map<Position, NodeType> nodes;
	private final Map<Position, Character> letters;

	Maze(List<String> diagram) {
		Map<Position, NodeType> nodes = HashMap.empty();
		Map<Position, Character> letters = HashMap.empty();
		for (int y = 0; y < diagram.size(); y++) {
			String line = diagram.get(y);
			for (int x = 0; x < line.length(); x++) {
				Position position = new Position(x, y);

				char ch = line.charAt(x);
				if (ch == '+') {
					nodes = nodes.put(position, NodeType.TURN);
				} else if (ch == '-' || ch == '|') {
					nodes = nodes.put(position, NodeType.CONTINUE);
				} else if (ch >= 'A' && ch <= 'Z') {
					nodes = nodes.put(position, NodeType.CONTINUE);
					letters = letters.put(position, ch);
				} else if (ch != ' ') {
					throw new IllegalArgumentException("Unexepected character: " + ch);
				}
			}
		}

		this.nodes = nodes;
		this.letters = letters;
	}

	Position getStart() {
		int minY = nodes.keySet().toStream()
				.map(Position::getY)
				.min().getOrElseThrow(IllegalStateException::new);

		return nodes.keySet().toStream()
				.find(position -> position.getY() == minY)
				.getOrElseThrow(IllegalStateException::new);
	}

	Position getNextPosition(Position previousPosition, Position currentPosition) {
		switch (getNode(currentPosition)) {
			case CONTINUE:
				return currentPosition.forwardFrom(previousPosition);
			case TURN: {
				Position nextPosition;
				nextPosition = currentPosition.left();
				if (isOkPosition(nextPosition, previousPosition)) return nextPosition;
				nextPosition = currentPosition.right();
				if (isOkPosition(nextPosition, previousPosition)) return nextPosition;
				nextPosition = currentPosition.up();
				if (isOkPosition(nextPosition, previousPosition)) return nextPosition;
				nextPosition = currentPosition.down();
				if (isOkPosition(nextPosition, previousPosition)) return nextPosition;

				throw new IllegalStateException("No exit from position " + currentPosition);
			}
			default:
				throw new IllegalStateException("No path at position " + currentPosition);
		}
	}

	private boolean isOkPosition(Position nextPosition, Position previousPosition) {
		return !nextPosition.equals(previousPosition) && getNode(nextPosition) != NodeType.NONE;
	}

	Option<Character> getLetter(Position position) {
		return letters.get(position);
	}

	NodeType getNode(Position position) {
		return nodes.get(position).getOrElse(NodeType.NONE);
	}

}
