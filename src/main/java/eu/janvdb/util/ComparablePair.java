package eu.janvdb.util;

public class ComparablePair<T1 extends Comparable<T1>, T2 extends Comparable<T2>> extends Pair<T1, T2> implements Comparable<ComparablePair<T1, T2>> {

	public ComparablePair(T1 v1, T2 v2) {
		super(v1, v2);
	}

	@Override
	public int compareTo(ComparablePair<T1, T2> other) {
		int result1 = getV1().compareTo(other.getV1());
		return result1 != 0 ? result1 : getV2().compareTo(other.getV2());
	}
}
