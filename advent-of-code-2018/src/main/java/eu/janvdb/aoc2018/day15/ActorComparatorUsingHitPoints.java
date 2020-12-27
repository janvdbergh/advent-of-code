package eu.janvdb.aoc2018.day15;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

public class ActorComparatorUsingHitPoints implements Comparator<Actor> {

	public static ActorComparatorUsingHitPoints INSTANCE = new ActorComparatorUsingHitPoints();

	@Override
	public int compare(Actor actor1, Actor actor2) {
		if (actor1.getHitPoints() != actor2.getHitPoints()) return actor1.getHitPoints() - actor2.getHitPoints();
		return PointComparator.INSTANCE.compare(actor1.getLocation(), actor2.getLocation());
	}

	public static SortedSet<Actor> newSortedSet() {
		return new TreeSet<>(INSTANCE);
	}
}
