package eu.janvdb.aocutil.java.shortestpath;

import java.util.Set;

public interface ShortestPath<T> {

	T getOrigin();

	Set<T> getReachablePoints();

	int distanceTo(T point);

	T stepTo(T point);

	void printRouteTo(T point);
}
