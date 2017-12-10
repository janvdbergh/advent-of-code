package eu.janvdb.util;

import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Stream;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RecipesTest {

	@Test
	public void returnsCorrectRecipes() {
		Stream<Map<String, Integer>> recipes = Recipes.getRecipes(List.of("item1", "item2", "item3"), 3);
		assertEquals(10, recipes.size());
	}

}