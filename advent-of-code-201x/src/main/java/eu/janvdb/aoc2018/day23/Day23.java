package eu.janvdb.aoc2018.day23;

import eu.janvdb.aoc2018.util.FileReader;
import eu.janvdb.aoc2018.util.Point3D;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Day23 {

	static final Point3D ORIGIN = new Point3D(0, 0, 0);
	private static final int NUMBER_OF_POINTS = 100;
	private static final int POINTS_TO_KEEP = 1;
	private static final int NUMBER_OF_GENERATIONS = 10_000_000;
	private static Random RANDOM = new Random();

	public static void main(String[] args) throws IOException {
		// <12984978,32351668,16335324>: 873 (61671970)
		// <13333512,32048906,16532479>: 902 (61914897)
		// <13210383,32125714,16332537>: 904 (61668634)
		//                                  < 61668634
		List<Nanobot> nanobots = FileReader.readStringFile(Day23.class, "day23.txt").stream()
				.map(Nanobot::new)
				.collect(Collectors.toList());
		Swarm swarm = new Swarm(nanobots);

		part1(swarm);

		part2(nanobots, swarm);
	}

	private static void part1(Swarm swarm) {
		Nanobot strongestNanobot = swarm.getStrongest();
		int numberInRange = swarm.getNumberInRange(strongestNanobot);
		System.out.println(numberInRange);
	}

	private static void part2(List<Nanobot> nanobots, Swarm swarm) {
		int minx = nanobots.stream().mapToInt(nanobot -> nanobot.getLocation().getX() - nanobot.getRadius()).min().orElseThrow();
		int maxx = nanobots.stream().mapToInt(nanobot -> nanobot.getLocation().getX() + nanobot.getRadius()).max().orElseThrow();
		int miny = nanobots.stream().mapToInt(nanobot -> nanobot.getLocation().getY() - nanobot.getRadius()).min().orElseThrow();
		int maxy = nanobots.stream().mapToInt(nanobot -> nanobot.getLocation().getY() + nanobot.getRadius()).max().orElseThrow();
		int minz = nanobots.stream().mapToInt(nanobot -> nanobot.getLocation().getZ() - nanobot.getRadius()).min().orElseThrow();
		int maxz = nanobots.stream().mapToInt(nanobot -> nanobot.getLocation().getZ() + nanobot.getRadius()).max().orElseThrow();
		SortedSet<ScoredPoint> sortedPoints = new TreeSet<>();
		List<ScoredPoint> pointList = new ArrayList<>();

		while (sortedPoints.size() < NUMBER_OF_POINTS) {
			Point3D point = new Point3D(random(minx, maxx), random(miny, maxy), random(minz, maxz));
			ScoredPoint scoredPoint = new ScoredPoint(point, swarm.getNumberInRange(point));
			if (sortedPoints.add(scoredPoint)) pointList.add(scoredPoint);
		}

		for (int i = 0; i < NUMBER_OF_GENERATIONS; i++) {
			int choice = RANDOM.nextInt(10);
			if (choice < 2) {
				Point3D point = new Point3D(random(minx, maxx), random(miny, maxy), random(minz, maxz));
				ScoredPoint scoredPoint = new ScoredPoint(point, swarm.getNumberInRange(point));
				replaceRandomPoint(sortedPoints, pointList, scoredPoint);
			} else if (choice < 6) {
				ScoredPoint scoredPoint = chooseRandomPoint(pointList);
				Point3D point = scoredPoint.getLocation();
				Point3D newPoint = new Point3D(point.getX() + random(minx / 3, maxx / 3), point.getY() + random(miny / 3, maxy / 3), point.getZ() + random(minz / 3, maxz / 3));
				ScoredPoint newScoredPoint = new ScoredPoint(newPoint, swarm.getNumberInRange(newPoint));
				replacePointIfBetter(sortedPoints, pointList, newScoredPoint);
			} else {
				ScoredPoint scoredPoint1;
				ScoredPoint scoredPoint2;
				scoredPoint1 = chooseRandomPoint(pointList);
				scoredPoint2 = chooseRandomPoint(pointList);
				Point3D middle = scoredPoint1.getLocation().middle(scoredPoint2.getLocation());
				ScoredPoint middleScoredPoint = new ScoredPoint(middle, swarm.getNumberInRange(middle));
				replacePointIfBetter(sortedPoints, pointList, middleScoredPoint);
			}

			if (i % (NUMBER_OF_GENERATIONS / 50) == 0) {
				ScoredPoint bestPoint = sortedPoints.iterator().next();
				System.out.printf("%10d: %s\n", i, bestPoint);
			}
		}

		ScoredPoint bestPoint = sortedPoints.iterator().next();
		System.out.printf("%10d: %s\n", NUMBER_OF_GENERATIONS, bestPoint);
	}

	private static int random(int min, int max) {
		return min + Day23.RANDOM.nextInt(max - min + 1);
	}

	private static void replaceRandomPoint(SortedSet<ScoredPoint> sortedPoints, List<ScoredPoint> pointList, ScoredPoint scoredPoint) {
		ScoredPoint pointToReplace = sortedPoints.stream().skip(POINTS_TO_KEEP + RANDOM.nextInt(NUMBER_OF_POINTS - POINTS_TO_KEEP)).findFirst().orElseThrow();
		replaceRandomPoint(sortedPoints, pointList, pointToReplace, scoredPoint);
	}

	private static void replacePointIfBetter(SortedSet<ScoredPoint> sortedPoints, List<ScoredPoint> pointList, ScoredPoint scoredPoint) {
		ScoredPoint pointToReplace = sortedPoints.stream().skip(sortedPoints.size() - 1).findFirst().orElseThrow();
		if (pointToReplace.compareTo(scoredPoint) > 0) {
			replaceRandomPoint(sortedPoints, pointList, pointToReplace, scoredPoint);
		}
	}

	private static void replaceRandomPoint(SortedSet<ScoredPoint> sortedPoints, List<ScoredPoint> pointList, ScoredPoint oldPoint, ScoredPoint newPoint) {
		if (sortedPoints.add(newPoint)) {
			sortedPoints.remove(oldPoint);
			int index = pointList.indexOf(oldPoint);
			pointList.set(index, newPoint);
		}
	}

	private static ScoredPoint chooseRandomPoint(List<ScoredPoint> pointList) {
		int index = RANDOM.nextInt(pointList.size() / 4 * 3);
		return pointList.get(index);
	}
}
