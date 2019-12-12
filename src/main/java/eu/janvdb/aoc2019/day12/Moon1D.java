package eu.janvdb.aoc2019.day12;

import java.util.Objects;

import io.vavr.collection.List;

public class Moon1D {

	private final int position;
	private final int velocity;

	public Moon1D(int position) {
		this.position = position;
		this.velocity = 0;
	}

	public Moon1D(int position, int velocity) {
		this.position = position;
		this.velocity = velocity;
	}

	public int getPosition() {
		return position;
	}

	public int getVelocity() {
		return velocity;
	}

	public Moon1D update(List<Moon1D> allMoons) {
		List<Moon1D> otherMoons = allMoons.filter(moon -> moon != this);
		int newVelocity = this.velocity +
				otherMoons.toStream()
						.map(moon -> Integer.compare(moon.position, this.position))
						.sum().intValue();

		int newPosition = this.position + newVelocity;
		return new Moon1D(newPosition, newVelocity);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Moon1D moon1D = (Moon1D) o;
		return position == moon1D.position && velocity == moon1D.velocity;
	}

	@Override
	public int hashCode() {
		return Objects.hash(position, velocity);
	}
}
