package eu.janvdb.aoc2019.day14;

public class ProductQuantity {

	private final String product;
	private final long quantity;

	public static ProductQuantity parse(String description) {
		// e.g. 12 VRPVC
		String[] parts = description.split("\\s+");
		return new ProductQuantity(parts[1], Long.parseLong(parts[0]));
	}

	public ProductQuantity(String product, long quantity) {
		if (quantity <= 0) {
			throw new IllegalArgumentException();
		}

		this.product = product;
		this.quantity = quantity;
	}

	public String getProduct() {
		return product;
	}

	public long getQuantity() {
		return quantity;
	}

	@Override
	public String toString() {
		return String.format("%d %s", quantity, product);
	}

	public ProductQuantity multiply(long factor) {
		return new ProductQuantity(product, quantity * factor);
	}
}
