package eu.janvdb.aoc2018.day23;

import eu.janvdb.aocutil.java.FileReader;
import eu.janvdb.aocutil.java.MathUtil;
import eu.janvdb.aocutil.java.Point3D;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Swarm {

	private final List<Nanobot> nanobots;

	public Swarm(List<Nanobot> nanobots) {
		this.nanobots = Collections.unmodifiableList(nanobots);
	}

	public static Swarm parse(String name) {
		List<Nanobot> nanobots = FileReader.readStringFile(Day23.class, name).stream()
				.map(Nanobot::parse)
				.collect(Collectors.toList());
		return new Swarm(nanobots);
	}

	public int size() {
		return nanobots.size();
	}

	public Nanobot getStrongest() {
		return nanobots.stream()
				.max(Comparator.comparing(Nanobot::getRadius))
				.orElseThrow();
	}

	public int findNumberOfNanobotsInRangeOf(Nanobot nanobot) {
		return (int) nanobots.stream().map(Nanobot::getCoordinate).filter(nanobot::isInRange).count();
	}

	public int numberOfNanobotsOverlapping(Cube cube) {
		return (int) nanobots.stream().filter(cube::overlaps).count();
	}

	public Cube getBoundingCube() {
		int minX = minBy(Point3D::getX);
		int maxX = maxBy(Point3D::getX);
		int centerX = (maxX + minX) / 2;
		int minY = minBy(Point3D::getY);
		int maxY = maxBy(Point3D::getY);
		int centerY = (maxY + minY) / 2;
		int minZ = minBy(Point3D::getZ);
		int maxZ = maxBy(Point3D::getZ);
		int centerZ = (maxZ + minZ) / 2;

		int radius = MathUtil.max(maxX - minX, maxY - minY, maxZ - minZ) / 2;

		return new Cube(new Point3D(centerX, centerY, centerZ), radius);
	}

	private int minBy(Function<Point3D, Integer> f) {
		return nanobots.stream()
				.mapToInt(nanobot -> f.apply(nanobot.getCoordinate()) - nanobot.getRadius())
				.min().orElseThrow();
	}

	private int maxBy(Function<Point3D, Integer> f) {
		return nanobots.stream()
				.mapToInt(nanobot -> f.apply(nanobot.getCoordinate()) + nanobot.getRadius())
				.max().orElseThrow();
	}
}
