package eu.janvdb.aoc2019.day12;

import eu.janvdb.aocutil.java.Point3D;
import io.vavr.collection.List;

import java.util.Objects;

class Moon {

	private final Moon1D x;
	private final Moon1D y;
	private final Moon1D z;

	public Moon(Moon1D x, Moon1D y, Moon1D z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Moon(int x, int y, int z) {
		this.x = new Moon1D(x, 0);
		this.y = new Moon1D(y, 0);
		this.z = new Moon1D(z, 0);
	}

	public Moon1D getX() {
		return x;
	}

	public Moon1D getY() {
		return y;
	}

	public Moon1D getZ() {
		return z;
	}

	public Point3D getPosition() {
		return new Point3D(x.getPosition(),y.getPosition(),z.getPosition());
	}

	public Point3D getVelocity() {
		return new Point3D(x.getVelocity(),y.getVelocity(),z.getVelocity());
	}

	public int getEnergy() {
		return getPosition().getManhattanDistanceFromOrigin() * getVelocity().getManhattanDistanceFromOrigin();
	}

	public Moon update(List<Moon> allMoons) {
		return new Moon(
				x.update(allMoons.map(Moon::getX)),
				y.update(allMoons.map(Moon::getY)),
				z.update(allMoons.map(Moon::getZ))
		);
	}

	@Override
	public String toString() {
		return "{position=" + getPosition() + ", velocity=" + getVelocity() + ", energy=" + getEnergy() + '}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Moon moon = (Moon) o;
		return x.equals(moon.x) && y.equals(moon.y) && z.equals(moon.z);
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y, z);
	}
}
