package eu.janvdb.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import javaslang.collection.List;
import javaslang.collection.Map;
import javaslang.collection.Stream;

public class RecipesTest {

	@Test
	public void returnsCorrectRecipes() {
		Stream<Map<String, Integer>> recipes = Recipes.getRecipes(List.of("item1", "item2", "item3"), 3);
		assertEquals(10, recipes.size());
	}

}