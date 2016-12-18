package eu.janvdb.util;


import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.collection.HashMap;
import javaslang.collection.HashSet;
import javaslang.collection.Map;
import javaslang.collection.Set;

public class Matrix<KeyType, ValueType> {

	private ValueType defaultValue;
	private Map<Tuple2<KeyType, KeyType>, ValueType> data = HashMap.empty();
	private Set<KeyType> keys = HashSet.empty();

	public Matrix(ValueType defaultValue) {
		this.defaultValue = defaultValue;
	}

	public void set(KeyType x, KeyType y, ValueType value) {
		data = data.put(Tuple.of(x, y), value);
		keys = keys.add(x).add(y);
	}

	public ValueType get(KeyType x, KeyType y) {
		return data.get(Tuple.of(x, y)).getOrElse(defaultValue);
	}

	public Set<KeyType> keys() {
		return keys;
	}

}
