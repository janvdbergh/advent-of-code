package eu.janvdb.aoc2018.day23;

import java.util.List;
import java.util.stream.Collectors;

public class PossibleSolution {

	private final Cube cube;
	private final Swarm swarm;
	private final int score;

	public PossibleSolution(Cube cube, Swarm swarm) {
		this.cube = cube;
		this.swarm = swarm;
		this.score = swarm.numberOfNanobotsOverlapping(cube);
	}

	public Cube getCube() {
		return cube;
	}

	public int getScore() {
		return score;
	}

	public List<PossibleSolution> split() {
		return cube.split().stream().map(cube -> new PossibleSolution(cube, swarm)).collect(Collectors.toList());
	}

	@Override
	public String toString() {
		return String.format("cube=%s, score=%d, distance=%d", cube, score, cube.getCoordinate().getManhattanDistanceFromOrigin());
	}
}
