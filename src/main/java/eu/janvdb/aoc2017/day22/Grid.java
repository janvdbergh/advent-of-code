package eu.janvdb.aoc2017.day22;

import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;

public class Grid {

	private Map<Position, State> infectedNodes;

	Grid(List<String> input) {
		infectedNodes = HashMap.empty();

		int height = input.length();
		int width = input.get(0).length();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (input.get(y).charAt(x) == '#') {
					setState(new Position(x - width / 2, y - height / 2), State.INFECTED);
				}
			}
		}
	}

	void setState(Position position, State newState) {
		if (newState == State.CLEAN) {
			infectedNodes = infectedNodes.remove(position);
		} else {
			infectedNodes = infectedNodes.put(position, newState);
		}
	}

	State getState(Position position) {
		return infectedNodes.get(position).getOrElse(State.CLEAN);
	}

	@Override
	public String toString() {
		int maxX = infectedNodes.keySet().map(Position::getX).map(Math::abs).max().getOrElse(0);
		int maxY = infectedNodes.keySet().map(Position::getY).map(Math::abs).max().getOrElse(0);
		int minX = -maxX;
		int minY = -maxY;

		StringBuilder builder = new StringBuilder();
		for (int y = minY; y <= maxY; y++) {
			for (int x = minX; x <= maxX; x++) {
				builder.append(getState(new Position(x, y)).getRepresentation());
			}
			builder.append('\n');
		}

		return builder.toString();
	}

	enum State {
		CLEAN('.'), WEAKENED('W'), INFECTED('#'), FLAGGED('F');

		private final char representation;

		State(char representation) {
			this.representation = representation;
		}

		char getRepresentation() {
			return representation;
		}
	}
}
