package eu.janvdb.aocutil.java.shortestpath;

import eu.janvdb.aocutil.java.Point2D;

import java.util.Set;

public interface ShortestPath {

	Point2D getOrigin();

	Set<Point2D> getReachablePoints();

	int distanceTo(Point2D point);

	Point2D stepTo(Point2D point);

}
