package org.DVitushkin.ingredient;

import org.DVitushkin.customexception.IngredientException;

public class CoffeeMachineIng extends Ingredient {
    private final int maxVolumeIngredient;

    public CoffeeMachineIng(String name, int count, int maxVolumeIngredient) {
        super(name, count);

        this.maxVolumeIngredient = maxVolumeIngredient;
    }

    @Override
    public void addCount(int newCount) throws IngredientException {
        if (this.count + newCount < 0) {
            throw new IngredientException(String.format("New count of <%s> been negative\n", this.getName()));
        } else if (newCount + this.count  > maxVolumeIngredient) {
            throw new IngredientException(String.format("Error: New count == <%d> of <%s>, been maximum permissible value == <%d>\n",
                                                        newCount + this.count,
                                                        this.getName(), 
                                                        maxVolumeIngredient));
        } else {
            this.count += newCount;
        }
    }
}
