package eu.janvdb.aoc2019.day14;

import static eu.janvdb.aoc2019.day14.Constants.PRODUCT_ORE;

public class PureReaction {

	private final long numberOfOreRequired;
	private final long numberOfProductProduced;
	private final String product;

	public PureReaction(long numberOfOreRequired, long numberOfProductProduced, String product) {
		this.numberOfOreRequired = numberOfOreRequired;
		this.numberOfProductProduced = numberOfProductProduced;
		this.product = product;
	}

	public String getProduct() {
		return product;
	}

	public long getNumberOfOreRequired() {
		return numberOfOreRequired;
	}

	public long getNumberOfProductProduced() {
		return numberOfProductProduced;
	}

	@Override
	public String toString() {
		return String.format("%d %s <= %d %s", numberOfProductProduced, product,numberOfOreRequired, PRODUCT_ORE);
	}
}
