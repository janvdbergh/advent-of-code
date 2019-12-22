package eu.janvdb.aoc2019.day23;

import java.math.BigInteger;

public interface Permutable<T extends Permutable<T>> {
	T dealIntoNewStack();
	T cut(BigInteger cut);
	T dealWithIncrement(BigInteger increment);
}
