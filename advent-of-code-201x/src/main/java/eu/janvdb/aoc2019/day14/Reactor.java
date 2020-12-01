package eu.janvdb.aoc2019.day14;

import io.vavr.Tuple;
import io.vavr.collection.List;

import java.util.HashMap;
import java.util.Map;

import static eu.janvdb.aoc2019.day14.Constants.PRODUCT_ORE;

public class Reactor {
	private final Map<String, Reaction> reactionsByProduct;
	private Map<String, Long> reactorState = new HashMap<>();
	private Map<String, Long> totalsProduced = new HashMap<>();

	public Reactor(List<Reaction> reactions) {
		this.reactionsByProduct = reactions
				.toMap(reaction -> Tuple.of(reaction.getOutput().getProduct(), reaction))
				.toJavaMap();
	}

	public long oreRequiredFor(ProductQuantity required) {
		totalsProduced.clear();
		reactorState.clear();

		getProductQuantity(required);
		return totalsProduced.get(PRODUCT_ORE);
	}

	private void getProductQuantity(ProductQuantity required) {
		if (getCurrentStock(required.getProduct()) < required.getQuantity()) {
			produce(required.getProduct(), required.getQuantity() - getCurrentStock(required.getProduct()));
		}

		updateCurrentStock(required.getProduct(), -required.getQuantity());
	}

	private void produce(String product, long quantity) {
		if (product.equals(PRODUCT_ORE)) {
			updateCurrentStock(product, quantity);
		} else {
			Reaction reaction = reactionsByProduct.get(product);
			long requiredAmount = (quantity + reaction.getOutput().getQuantity() - 1) / reaction.getOutput().getQuantity();
			reaction.getInputs().map(productQuantity -> productQuantity.multiply(requiredAmount)).forEach(this::getProductQuantity);
			updateCurrentStock(reaction.getOutput().getProduct(), requiredAmount * reaction.getOutput().getQuantity());
		}
	}

	private long getCurrentStock(String product) {
		return reactorState.getOrDefault(product, 0L);
	}

	private void updateCurrentStock(String product, long delta) {
		reactorState.put(product, getCurrentStock(product) + delta);
		if (delta > 0) {
			totalsProduced.put(product, totalsProduced.getOrDefault(product, 0L) + delta);
		}
	}
}
