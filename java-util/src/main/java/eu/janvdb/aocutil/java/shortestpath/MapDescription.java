package eu.janvdb.aocutil.java.shortestpath;

import eu.janvdb.aocutil.java.Point2D;

import java.util.List;

public interface MapDescription {

	Point2D getOrigin();

	List<Point2D> getNeighbours(Point2D point);

	int getDistance(Point2D from, Point2D to);
}
