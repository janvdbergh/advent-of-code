package eu.janvdb.aocutil.java.shortestpath;

import java.util.List;

public interface MapDescription<T> {

	T getOrigin();

	List<T> getNeighbours(T point);

	int getDistance(T from, T to);

	default String getDescription(T point) {
		return point.toString();
	}
}
