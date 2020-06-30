package eu.janvdb.aoc2018.day23;

import java.util.List;

import eu.janvdb.aoc2018.util.Point3D;

class Swarm {

	private List<Nanobot> nanobots;

	Swarm(List<Nanobot> nanobots) {
		this.nanobots = nanobots;
	}

	Nanobot getStrongest() {
		long maxRadius = nanobots.stream()
				.mapToLong(Nanobot::getRadius)
				.max()
				.orElseThrow();
		return nanobots.stream()
				.filter(nanobot -> nanobot.getRadius() == maxRadius)
				.findAny()
				.orElseThrow();
	}

	int getNumberInRange(Nanobot nanobot) {
		return (int) nanobots.stream()
				.filter(nanobot::isInRange)
				.count();
	}

	int getNumberInRange(Point3D point) {
		return (int) nanobots.stream()
				.filter(nanobot -> nanobot.isInRange(point))
				.count();
	}


}
