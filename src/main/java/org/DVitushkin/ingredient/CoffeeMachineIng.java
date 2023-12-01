package org.DVitushkin.ingredient;

import org.DVitushkin.customexception.IngredientException;

public class CoffeeMachineIng extends Ingredient {
    private final int maxVolumeIngredient;

    public CoffeeMachineIng(String name, int count, int maxVolumeIngredient) {
        super(name, count);

        this.maxVolumeIngredient = maxVolumeIngredient;
    }

    @Override
    public void setCount(int newCount) throws IngredientException {
        if (newCount < 0) {
            throw new IngredientException(String.format("New count of <%s> been negative", this.getName()));
        } else if (newCount > maxVolumeIngredient) {
            throw new IngredientException(String.format("New count == <%d> of <%s>, been maximum permissible value == <%d>",
                                                        newCount, 
                                                        this.getName(), 
                                                        maxVolumeIngredient));
        } else {
            this.count = newCount;
        }
    }
}
