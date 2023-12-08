package org.DVitushkin.ingredient;

import org.DVitushkin.customexception.IngredientException;

public class Ingredient {
    protected final String name;
    protected int count;

    public Ingredient(String name, int count) {
        this.name = name;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public void addCount(int newCount) throws IngredientException {
        if (newCount < 0) {
            throw new IngredientException("New count been negative");
        }
        this.count += newCount;
    }
}

