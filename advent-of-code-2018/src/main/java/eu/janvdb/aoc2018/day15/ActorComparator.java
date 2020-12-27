package eu.janvdb.aoc2018.day15;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

public class ActorComparator implements Comparator<Actor> {

	public static ActorComparator INSTANCE = new ActorComparator();

	@Override
	public int compare(Actor actor1, Actor actor2) {
		return PointComparator.INSTANCE.compare(actor1.getLocation(), actor2.getLocation());
	}

	public static SortedSet<Actor> newSortedSet() {
		return new TreeSet<>(INSTANCE);
	}
}
