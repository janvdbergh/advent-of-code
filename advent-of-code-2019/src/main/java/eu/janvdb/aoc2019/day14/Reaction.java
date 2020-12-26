package eu.janvdb.aoc2019.day14;

import io.vavr.collection.List;

public class Reaction {

	private final List<ProductQuantity> inputs;
	private final ProductQuantity output;

	public static Reaction parse(String description) {
		// e.g. 12 VRPVC, 27 CNZTR => 2 XDBXC
		String[] parts = description.split("\\s*=>\\s*");
		ProductQuantity output = ProductQuantity.parse(parts[1]);
		List<ProductQuantity> inputs = List.of(parts[0].split("\\s*,\\s*")).map(ProductQuantity::parse);
		return new Reaction(inputs, output);
	}

	public Reaction(List<ProductQuantity> inputs, ProductQuantity output) {
		this.inputs = inputs;
		this.output = output;
	}

	public List<ProductQuantity> getInputs() {
		return inputs;
	}

	public ProductQuantity getOutput() {
		return output;
	}

	public Reaction multiply(long factor) {
		return new Reaction(
				inputs.map(productQuantity -> productQuantity.multiply(factor)),
				output.multiply(factor)
		);
	}

	public Reaction replaceInput(String productToReplace, ProductQuantity newInput) {
		ProductQuantity input = inputs.find(i -> i.getProduct().equals(productToReplace)).getOrNull();
		List<ProductQuantity> newInputs = inputs.replace(input, newInput);
		return new Reaction(newInputs, output);
	}

	@Override
	public String toString() {
		return String.format("%s <= %s", output, inputs.mkString(", "));
	}
}
