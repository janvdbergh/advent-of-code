package eu.janvdb.aoc2017.day13;

import io.vavr.Tuple;
import io.vavr.collection.Map;

class ScannerState {

    private final int delay;
    private final Map<Integer, Integer> depthPerLayer;
    private final Map<Integer, Integer> states;
    private final Map<Integer, Direction> directions;

    ScannerState(Map<Integer, Integer> depthPerLayer) {
        this.delay = 0;
        this.depthPerLayer = depthPerLayer;
        this.states = depthPerLayer.map((layer, depth) -> Tuple.of(layer, 0));
        this.directions = depthPerLayer.map((layer, depth) -> Tuple.of(layer, Direction.DOWN));
    }

    private ScannerState(int delay, Map<Integer, Integer> depthPerLayer, Map<Integer, Integer> states, Map<Integer, Direction> directions) {
        this.delay = delay;
        this.depthPerLayer = depthPerLayer;
        this.states = states;
        this.directions = directions;
    }

    ScannerState nextState() {
        Map<Integer, Direction> newDirections = directions.map((layer, direction) -> Tuple.of(layer, getNextDirection(layer, direction)));
        Map<Integer, Integer> nextStates = states.map((layer, position) -> Tuple.of(layer, newDirections.get(layer).getOrElse(Direction.DOWN) == Direction.DOWN ? position + 1 : position - 1));

        return new ScannerState(delay + 1, depthPerLayer, nextStates, newDirections);
    }

    private Direction getNextDirection(int layer, Direction direction) {
        int position = getPosition(layer);
        if (position == 0) return Direction.DOWN;
        if (position == getDepth(layer) - 1) return Direction.UP;
        return direction;
    }

    int getDelay() {
        return delay;
    }

    int getDepth(int layer) {
        return depthPerLayer.get(layer).getOrElse(-1);
    }

    int getPosition(int layer) {
        return states.get(layer).getOrElse(-1);
    }

    enum Direction {UP, DOWN}
}
