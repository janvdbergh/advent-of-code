package eu.janvdb.util;

public class Pair<T1, T2> {

	private final T1 v1;
	private final T2 v2;

	public Pair(T1 v1, T2 v2) {
		this.v1 = v1;
		this.v2 = v2;
	}

	public T1 getV1() {
		return v1;
	}

	public T2 getV2() {
		return v2;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Pair<?, ?> pair = (Pair<?, ?>) o;
		return v1.equals(pair.v1) && v2.equals(pair.v2);
	}

	@Override
	public int hashCode() {
		int result = v1.hashCode();
		result = 31 * result + v2.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "(" + v1 + "," + v2 + ")";
	}
}
