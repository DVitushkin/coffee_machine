package org.DVitushkin;

import org.DVitushkin.machine.BeverageService;
import org.DVitushkin.beverage.Beverage;
import org.DVitushkin.machine.CoffeeIngredientService;
import org.DVitushkin.ingredient.CoffeeMachineIng;
import org.DVitushkin.ingredient.Ingredient;
import org.DVitushkin.machine.Drink;
import org.DVitushkin.machine.Machine;

import java.util.ArrayList;

public class App {
    private static ArrayList<CoffeeMachineIng> collectIngList(){
        ArrayList<CoffeeMachineIng> ingList = new ArrayList<CoffeeMachineIng>();
        ingList.add(new CoffeeMachineIng("water", 200, 200));
        ingList.add(new CoffeeMachineIng("milk", 200, 200));
        ingList.add(new CoffeeMachineIng("coffee", 50, 50));
        return ingList;
    }

    private static ArrayList<Beverage> collectBvgList(){
        ArrayList<Beverage> bvgList = new ArrayList<Beverage>();
        bvgList.add(App.makeEspressoRecipe());
        bvgList.add(App.makeCappuccinoRecipe());

        return bvgList;
    }

    private static Beverage makeEspressoRecipe() {
        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
        ingredients.add(new Ingredient("water", 20));
        ingredients.add(new Ingredient("coffee", 5));
        return new Beverage(Drink.ESPRESSO, ingredients);
    }

    private static Beverage makeCappuccinoRecipe() {
        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
        ingredients.add(new Ingredient("water", 15));
        ingredients.add(new Ingredient("coffee", 5));
        ingredients.add(new Ingredient("milk", 5));
        return new Beverage(Drink.CAPPUCCINO, ingredients);
    }

    public static void main( String[] args ) {
        CoffeeIngredientService ingList = new CoffeeIngredientService(App.collectIngList());
        BeverageService bvgList = new BeverageService(App.collectBvgList());
        Machine mc = new Machine(ingList, bvgList, 10);

        mc.entryPoint();
    }
}
