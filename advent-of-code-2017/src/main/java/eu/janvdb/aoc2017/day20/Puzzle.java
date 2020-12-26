package eu.janvdb.aoc2017.day20;

import eu.janvdb.aocutil.java.InputReader;
import io.vavr.collection.List;
import io.vavr.collection.Set;

public class Puzzle {

	private static final int STEPS = 1000;

	public static void main(String[] args) {
		List<Particle> particles = InputReader.readInput(Puzzle.class.getResource("input.txt"))
				.zipWithIndex()
				.map(tuple -> new Particle(tuple._2, tuple._1))
				.toList();

		part1(particles);
		part2(particles);
	}

	private static void part1(List<Particle> particles) {
		for (int i = 0; i < STEPS; i++) {
			long minDistance = particles.toStream()
					.map(Particle::getManhattanDistance)
					.min()
					.getOrElseThrow(IllegalStateException::new);

			Particle closestParticle = particles.toStream()
					.find(particle -> particle.getManhattanDistance() == minDistance)
					.getOrElseThrow(IllegalStateException::new);

			particles = particles.toStream()
					.map(Particle::step)
					.toList();

			System.out.println(closestParticle);
		}
	}

	private static void part2(List<Particle> particles) {
		for (int i = 0; i < STEPS; i++) {
			Set<Vector> collidingPositions = particles
					.groupBy(Particle::getPosition)
					.filter(tuple -> tuple._2.size() > 1)
					.map(tuple -> tuple._1)
					.toSet();

			particles = particles.toStream()
					.filter(particle -> !collidingPositions.contains(particle.getPosition()))
					.map(Particle::step)
					.toList();

			System.out.println(particles.size());
		}
	}
}
