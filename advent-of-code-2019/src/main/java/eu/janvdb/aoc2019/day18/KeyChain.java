package eu.janvdb.aoc2019.day18;

import java.util.BitSet;
import java.util.Objects;
import java.util.stream.Collectors;

public class KeyChain {
	private final int numberOfKeys;
	private final BitSet keys;

	public static KeyChain empty(int numberOfKeys) {
		BitSet keys = new BitSet(numberOfKeys);
		return new KeyChain(keys, numberOfKeys);
	}

	public static KeyChain all(int numberOfKeys) {
		BitSet keys = new BitSet(numberOfKeys);
		keys.set(0, numberOfKeys);
		return new KeyChain(keys, numberOfKeys);
	}

	private KeyChain(BitSet keys, int numberOfKeys) {
		this.keys = keys;
		this.numberOfKeys = numberOfKeys;
	}

	public boolean hasKey(char symbol) {
		return keys.get(getIndex(symbol));
	}

	public KeyChain setKey(char symbol) {
		if (hasKey(symbol)) return this;

		BitSet newKeys = (BitSet) keys.clone();
		newKeys.set(getIndex(symbol));
		return new KeyChain(newKeys, numberOfKeys);
	}

	public KeyChain removeKey(char symbol) {
		if (!hasKey(symbol)) return this;

		BitSet newKeys = (BitSet) keys.clone();
		newKeys.clear(getIndex(symbol));
		return new KeyChain(newKeys, numberOfKeys);
	}

	private int getIndex(char symbol) {
		return Character.toLowerCase(symbol) - 'a';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		KeyChain keyChain = (KeyChain) o;
		return keys.equals(keyChain.keys);
	}

	@Override
	public int hashCode() {
		return Objects.hash(keys);
	}

	@Override
	public String toString() {
		return keys.stream().mapToObj(x -> String.valueOf((char) (x + 'a'))).collect(Collectors.joining());
	}

	public boolean isComplete() {
		return keys.cardinality() == numberOfKeys;
	}
}
