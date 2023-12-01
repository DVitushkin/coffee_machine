package org.DVitushkin.beverage;

import org.DVitushkin.ingredient.Ingredient;
import org.DVitushkin.machine.Drink;

import java.util.List;

public class Beverage {
    private Drink name;
    private List<Ingredient> ingredients;

    public Beverage(Drink name, List<Ingredient>  ingredients) {

        this.name = name;
        this.ingredients = ingredients;
    }

    public Drink getName() {
        return this.name;
    }

    public void setIngredients(List<Ingredient>  ingredients) {
        this.ingredients = ingredients;
    }

    public List<Ingredient>  getIngredients() {
        return this.ingredients;
    }
}
