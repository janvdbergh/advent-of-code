package eu.janvdb.util;

import javaslang.Function2;
import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.Tuple3;
import javaslang.collection.Map;
import javaslang.collection.Seq;
import javaslang.collection.Set;
import javaslang.collection.Stream;

public class Matrix<KeyType, ValueType> {

	private final Function2<KeyType, KeyType, ValueType> valueCalculator;
	private final Map<Tuple2<KeyType, KeyType>, ValueType> data;
	private final Set<KeyType> keys;

	@SafeVarargs
	public Matrix(Function2<KeyType, KeyType, ValueType> valueCalculator, Tuple3<KeyType, KeyType, ValueType>... values) {
		this(valueCalculator, Stream.of(values));
	}

	public Matrix(Function2<KeyType, KeyType, ValueType> valueCalculator, Seq<Tuple3<KeyType, KeyType, ValueType>> values) {
		this.valueCalculator = valueCalculator;
		this.data = values.toMap(value -> Tuple.of(Tuple.of(value._1, value._2), value._3));
		this.keys = this.data.toStream()
				.flatMap(dataTuple -> Stream.of(dataTuple._1._1, dataTuple._1._2))
				.toSet();
	}

	private Matrix(Function2<KeyType, KeyType, ValueType> valueCalculator, Map<Tuple2<KeyType, KeyType>, ValueType> data, Set<KeyType> keys) {
		this.valueCalculator = valueCalculator;
		this.data = data;
		this.keys = keys;
	}

	public Matrix<KeyType, ValueType> set(KeyType x, KeyType y, ValueType value) {
		return new Matrix<>(
				valueCalculator,
				data.put(Tuple.of(x, y), value),
				keys.add(x).add(y)
		);
	}

	public ValueType get(KeyType x, KeyType y) {
		return data.get(Tuple.of(x, y)).getOrElse(valueCalculator.apply(x, y));
	}

	public Set<KeyType> keys() {
		return keys;
	}

	public Seq<ValueType> values() {
		return data.values();
	}

}
