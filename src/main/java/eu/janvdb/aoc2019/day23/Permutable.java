package eu.janvdb.aoc2019.day23;

public interface Permutable<T extends Permutable<T>> {
	T dealIntoNewStack();
	T cut(int numberToCut);
	T dealWithIncrement(int increment);
}
