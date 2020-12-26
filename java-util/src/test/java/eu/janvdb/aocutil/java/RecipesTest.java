package eu.janvdb.aocutil.java;

import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Stream;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RecipesTest {

	@Test
	public void returnsCorrectRecipes() {
		Stream<Map<String, Integer>> recipes = Recipes.getRecipes(List.of("item1", "item2", "item3"), 3);
		assertThat(recipes.size()).isEqualTo(10);
	}

}