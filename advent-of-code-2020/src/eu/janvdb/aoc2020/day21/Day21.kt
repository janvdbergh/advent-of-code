package eu.janvdb.aoc2020.day21

import eu.janvdb.aoc2020.common.MatchFinder
import eu.janvdb.aoc2020.common.readLines

val INGREDIENT_LIST_REGEX = Regex("([^()]+) \\(contains ([^()]+)\\)")

fun main() {
	val recipes = readRecipes()
	val ingredients = recipes.getIngredients().toList()
	val allergens = recipes.getAllergens().toList()

	val ingredientByAllergen =
		MatchFinder.findMatch(allergens, ingredients) { allergen, ingredient -> recipes.matches(ingredient, allergen) }
	val ingredientsWithoutAllergen = ingredients.minus(ingredientByAllergen.values)

	val occurrencesOfIngredientsWithoutAllergen = ingredientsWithoutAllergen.map { ingredient ->
		recipes.recipes.count { recipe -> recipe.ingredients.contains(ingredient) }
	}.sum()
	println(occurrencesOfIngredientsWithoutAllergen)

	val ingredientsWithAllergenSorted = allergens.sorted().map { ingredientByAllergen[it] }.joinToString(separator = ",")
	println(ingredientsWithAllergenSorted)
}

fun readRecipes(): Recipes {
	return Recipes(
		readLines("input21.txt")
			.map { INGREDIENT_LIST_REGEX.matchEntire(it)!! }
			.map { Recipe(it.groupValues[1].split(" ").toSet(), it.groupValues[2].split(", ").toSet()) }
	)
}

data class Recipe(val ingredients: Set<String>, val allergens: Set<String>)

data class Recipes(val recipes: List<Recipe>) {
	fun getIngredients(): Set<String> = recipes.flatMap { it.ingredients }.toSet()
	fun getAllergens(): Set<String> = recipes.flatMap { it.allergens }.toSet()

	fun matches(ingredient: String, allergen: String): Boolean {
		for (recipe in recipes) {
			if (recipe.allergens.contains(allergen) && !recipe.ingredients.contains(ingredient)) return false
		}
		return true
	}
}