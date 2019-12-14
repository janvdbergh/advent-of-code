package eu.janvdb.aoc2019.day14;

import io.vavr.Tuple;
import io.vavr.collection.List;

import java.util.HashMap;
import java.util.Map;

import static eu.janvdb.aoc2019.day14.Constants.PRODUCT_ORE;

public class Reactor {
	private final Map<String, Reaction> reactionsByProduct;
	private Map<String, Long> reactorState = new HashMap<>();
	public static final long INITIAL_VALUE = 1_000_000_000L;

	public Reactor(List<Reaction> reactions) {
		this.reactionsByProduct = reactions
				.toMap(reaction -> Tuple.of(reaction.getOutput().getProduct(), reaction))
				.toJavaMap();
	}

	public long oreRequiredFor(ProductQuantity required) {
		initializeReactor(INITIAL_VALUE);
		getProductQuantity(required);
		return INITIAL_VALUE - getCurrentStock(PRODUCT_ORE);
	}

	public long maxProductForOre(long ore, ProductQuantity product) {
		initializeReactor(ore);

		long itemsProduced = 0L;
		long start = System.currentTimeMillis();
		while (true) {
			try {
				getProductQuantity(product);
				itemsProduced++;
				if (itemsProduced % 25000 == 0) {
					System.out.printf("[%d] %s%% left.\n", (System.currentTimeMillis() - start) / 1000, 100L * getCurrentStock(PRODUCT_ORE) / ore);
				}
			} catch (NotEnoughOreException e) {
				return itemsProduced;
			}
		}
	}

	private void getProductQuantity(ProductQuantity required) {
		while (getCurrentStock(required.getProduct()) < required.getQuantity()) {
			produce(required.getProduct());
		}

		updateCurrentStock(required.getProduct(), -required.getQuantity());
	}

	private void produce(String product) {
		if (product.equals(PRODUCT_ORE)) throw new NotEnoughOreException();

		Reaction reaction = reactionsByProduct.get(product);
		reaction.getInputs().forEach(this::getProductQuantity);
		updateCurrentStock(reaction.getOutput().getProduct(), reaction.getOutput().getQuantity());
	}

	private void initializeReactor(long ore) {
		reactorState.clear();
		reactorState.put(PRODUCT_ORE, ore);
	}

	private long getCurrentStock(String product) {
		return reactorState.getOrDefault(product, 0L);
	}

	private void updateCurrentStock(String product, long delta) {
		reactorState.put(product, getCurrentStock(product) + delta);
	}
}
