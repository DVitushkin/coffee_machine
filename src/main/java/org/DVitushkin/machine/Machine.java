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

    private void serveBeverage(Drink name) throws MachineException {
        if (this.cleanliness == 0) {
            throw new MachineException("Sorry but Coffee machine need to be clean");
        }

        System.out.printf("Please enter now many cup of %s, you want\n", name);
        int cupCount = stream.nextInt();

        Beverage beverage = this.getBeverageByName(name);
        for (Ingredient bvIng : beverage.getIngredients()) {
            Ingredient cmIng = this.getIngredientByName(bvIng.getName());

            try {
                cmIng.addCount(-1*(bvIng.getCount() * cupCount));
            } catch (IngredientException e) {
                throw new MachineException(e.getMessage());
            }
        }

        this.cleanliness -= cupCount;
    }

    private void choiceBeverage() throws MachineException {
        System.out.println("Выберите один из доступных напитков\n");
        for (Beverage bvg : this.beverageList) {
            System.out.printf("%d - %s\n", bvg.getName().ordinal()+1, bvg.getName());
        }


        int id = stream.nextInt();
        Drink drink = Drink.getDrinkById(id);

        try {
            this.serveBeverage(drink);
        } catch (MachineException e) {
            throw new MachineException(e.getMessage());
        }
    }

    private void handleFunc(int cid) throws MachineException {
        Controls cmd = Controls.getCommandByCid(cid);
        if (cmd == null) {
            throw new MachineException(String.format("Was entered incorrect command: <%d>", cid));
        }

        int count; // ?????
        switch (cmd) {
            case START_MACHINE:
                this.switchOnOf();

                logger.info("Button was pushed");
                break;
            case ADD_WATER:
                System.out.println("Please enter count of ingredient");
                count = stream.nextInt();

                try {
                    this.addIngredients("water", count);
                } catch (MachineException e) {
                    sendErrResponse(e);

                }

                logger.info(String.format("To <%s> was added <%d>", "water", count));
                break;
            case ADD_COFFEE:
                System.out.println("Please enter count of ingredient");
                count = stream.nextInt();

                try {
                    this.addIngredients("coffee", count);
                } catch (MachineException e) {
                    sendErrResponse(e);
                }

                logger.info(String.format("To <%s> was added <%d>", "coffee", count));
                break;
            case ADD_MILK:
                System.out.println("Please enter count of ingredient");
                count = stream.nextInt();

                try {
                    this.addIngredients("milk", count);
                } catch (MachineException e) {
                    sendErrResponse(e);
                }

                logger.info(String.format("To <%s> was added <%d>", "milk", count));
                break;

            case CHECK_SYSTEM:
                System.out.printf("How is %d cleanliness\n", this.cleanliness);
                this.showIngredients();
                break;
            case CHOICE_BEVERAGE:

                try {
                    this.choiceBeverage();
                } catch (MachineException e) {
                    sendErrResponse(e);
                }

                logger.info(String.format("Was cooked <%s>", "STUB"));
                break;
            case CREATE_PROFILE:
                // TODO
                logger.info("Was created profile");
                break;
            case CHOICE_PROFILE:
                // TODO
                logger.info("Was choice profile ");
                break;
            case SHOW_RECIPE:
                // TODO show Bevarge class of choicen recipe

                logger.info("Was showed recipe");
                break;
        }

    }

    private boolean loadMainMenu() {
        System.out.println("""
                        Кофемашина влкючена
                        Введите цифру соответсвующей команды:
                                        1 - Включить|Выключить
                                        2 - Добавить воду
                                        3 - Добавить кофе
                                        4 - Добавить молоко
                                        5 - Проверка состояния
                                        6 - выбрать напиток
                                        7 - Создать профиль
                                        8 - Выбрать профиль
                                        9 - Список рецептов
                        """
        );

        int cid = stream.nextInt();
        try {
            this.handleFunc(cid);
        } catch (MachineException e) {
            System.out.println(e.getMessage());
        }

        return this.onOffButton;
    }

    public void entryPoint() throws MachineException {
        try {
            this.greeting();
        } catch (MachineException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
        boolean flag = true;
        while (flag){
            flag = this.loadMainMenu();
        }
    }
}
