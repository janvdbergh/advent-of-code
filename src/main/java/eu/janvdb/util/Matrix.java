package eu.janvdb.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Matrix<KeyType, ValueType> {

	private ValueType defaultValue;
	private Map<Pair<KeyType, KeyType>, ValueType> data = new HashMap<>();
	private Set<KeyType> keys = new HashSet<>();

	public Matrix(ValueType defaultValue) {
		this.defaultValue = defaultValue;
	}

	public void set(KeyType x, KeyType y, ValueType value) {
		data.put(new Pair<>(x, y), value);
		keys.add(x);
		keys.add(y);
	}

	public ValueType get(KeyType x, KeyType y) {
		ValueType result = data.get(new Pair<>(x, y));
		return result != null ? result : defaultValue;
	}

	public Set<KeyType> getKeys() {
		return keys;
	}

}
