package org.DVitushkin.machine;

public enum Drink {
    ESPRESSO(1),
    CAPPUCCINO(2);

    private final int id;

    Drink(int id) { this.id = id; }

    public static Drink getDrinkById(int id) {
        return Drink.values()[id - 1];
    }
}
