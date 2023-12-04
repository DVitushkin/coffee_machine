package org.DVitushkin.machine;

import org.DVitushkin.beverage.Beverage;
import org.DVitushkin.customexception.IngredientException;
import org.DVitushkin.ingredient.CoffeeMachineIng;
import org.DVitushkin.ingredient.Ingredient;
import org.DVitushkin.customexception.MachineException;

import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class Machine {
    private final List<CoffeeMachineIng> ingredientList;
    private final List<Beverage> beverageList;

    private static final Scanner stream;
    private static final Logger logger;

    private int cleanliness;
    private boolean onOffButton;

    static {
        stream = new Scanner(System.in);
        logger = Logger.getLogger("Coffee Machine");
    }

    public Machine(List<CoffeeMachineIng> ingredientList, List<Beverage> beverageList, int cleanlinessCount) {
        this.ingredientList = ingredientList;
        this.beverageList = beverageList;

        this.cleanliness = cleanlinessCount;
        this.onOffButton = false;
    }

    private void greeting() throws MachineException {
        System.out.println("Greeting!\n1 - On|Off");
        int cmd = stream.nextInt();
        if (cmd == 1) {
            this.switchOnOf();
        } else {
            throw new MachineException(String.format("Was sent incorrect command: <%d>", cmd));
        }
    }

    private void switchOnOf() {
        this.onOffButton = !this.onOffButton;
    }

    private static void sendErrResponse(MachineException errMsg) {
        System.out.printf("Sorry but something was wrong:\n %s", errMsg.getMessage());
    }

    private void cleanMachine() throws MachineException {
        if (this.cleanliness == 10) {
            throw new MachineException("Machine has already cleaned");
        }

        this.cleanliness = 10;
    }

    public void addIngredients(String name, int count) throws MachineException {
        Ingredient ing = this.getIngredientByName(name);
        try {
            ing.addCount(count);
        } catch (IngredientException e) {
            throw new MachineException(e.getMessage());
        }
    }

    public void showIngredients() {
        for (Ingredient ing : this.ingredientList) {
            System.out.printf("%s == %d%n", ing.getName(), ing.getCount());
        }
    }

    private Beverage getBeverageByName(Drink name) {
        for (Beverage beverage : this.beverageList) {
            if (name.equals(beverage.getName())) {
                return beverage;
            }
        }
        return null;
    }

    private Ingredient getIngredientByName(String name) {
        for (Ingredient ingredient : this.ingredientList) {
            if (name.equals(ingredient.getName())) {
                return ingredient;
            }
        }
        return null;
    }
}
