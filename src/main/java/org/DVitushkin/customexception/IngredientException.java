package org.DVitushkin.customexception;

public class IngredientException extends Exception {
    public IngredientException(String message) {
        super(String.format("[Ingredient Exception]\n%s", message));
    }
}
