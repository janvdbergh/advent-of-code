package eu.janvdb.aoc2019.day11;

import eu.janvdb.util.Direction;
import eu.janvdb.util.Point2D;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;

import java.io.PrintStream;

public class Robot {

	private final BehaviorSubject<Long> output = BehaviorSubject.create();

	private Point2D location;
	private Direction direction;
	private Map<Point2D, Long> colors;

	public Robot(long startValue) {
		this.location = new Point2D(0, 0);
		this.direction = Direction.NORTH;
		this.colors = HashMap.of(location, startValue);
	}

	public Observable<Long> getOutput() {
		return output;
	}

	public void connectInput(Observable<Long> input) {
		input
				.buffer(2)
				.subscribe(values -> {
					long color = values.get(0);
					long action = values.get(1);
					colors = colors.put(location, color);
					switch ((int) action) {
						case 0:
							direction = direction.left();
							break;
						case 1:
							direction = direction.right();
							break;
						default:
							throw new IllegalArgumentException("Invalid action " + action);
					}
					location = location.step(direction, 1);
					outputCurrentColor();
				});
	}

	public void print(PrintStream out) {
		int minX = colors.toStream().map(Tuple2::_1).map(Point2D::getX).min().getOrElse(0);
		int maxX = colors.toStream().map(Tuple2::_1).map(Point2D::getX).max().getOrElse(0);
		int minY = colors.toStream().map(Tuple2::_1).map(Point2D::getY).min().getOrElse(0);
		int maxY = colors.toStream().map(Tuple2::_1).map(Point2D::getY).max().getOrElse(0);

		for (int y = minY; y <= maxY; y++) {
			for (int x = minX; x <= maxX; x++) {
				long color = colors.get(new Point2D(x,y)).getOrElse(0L);
				out.print(color == 0 ? '.' : '#');
			}
			out.println();
		}
	}

	public void start() {
		outputCurrentColor();
	}

	private void outputCurrentColor() {
		output.onNext(colors.getOrElse(location, 0L));
	}

	public Map<Point2D, Long> getColors() {
		return colors;
	}
}
