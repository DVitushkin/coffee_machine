package org.DVitushkin.machine;

import org.DVitushkin.customexception.IngredientException;
import org.DVitushkin.customexception.MachineException;
import org.DVitushkin.ingredient.CoffeeMachineIng;
import org.DVitushkin.ingredient.Ingredient;

import java.util.List;

public class CoffeeIngredientService {
    private final List<CoffeeMachineIng> ingredientList;

    public CoffeeIngredientService(List<CoffeeMachineIng> ingredientList) {
        this.ingredientList = ingredientList;
    }

    public void addIngredients(String name, int count) throws IngredientException {
        Ingredient ing = this.getIngredientByName(name);
        try {
            ing.addCount(count);
        } catch (IngredientException e) {
            throw new IngredientException(e.getMessage());
        }
    }

    public void showIngredients() {
        for (Ingredient ing : this.ingredientList) {
            System.out.printf("%s == %d%n", ing.getName(), ing.getCount());
        }
    }

    public Ingredient getIngredientByName(String name) {
        for (Ingredient ingredient : this.ingredientList) {
            if (name.equals(ingredient.getName())) {
                return ingredient;
            }
        }
        return new Ingredient(name, 0);
    }
}
