package org.DVitushkin.machine;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import org.DVitushkin.customexception.ProfileException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.DVitushkin.beverage.Beverage;
import org.DVitushkin.ingredient.Ingredient;
import org.DVitushkin.profile.Profile;
import org.DVitushkin.customexception.MachineException;
import org.DVitushkin.customexception.IngredientException;

public class Machine {
    private final CoffeeIngredientService ingredientList;
    private final BeverageService beverageList;
    private final ProfileService profiles;

    private static final Scanner stream = new Scanner(System.in);
    private static final Logger logger = LogManager.getLogger();

    private int cleanliness;
    private boolean onOffButton;
    private Profile activeProfile;

    private final String DEFAULT_MENU = """
                                    Coffee machine is on
                                    Enter the number of the corresponding command:
                                                    1 - On|Off
                                                    2 - Add water
                                                    3 - Add coffee
                                                    4 - Add milk
                                                    5 - Check system
                                                    6-  Clean the machine
                                                    7 - Choice beverage
                                                    8 - Show recipe
                                                    9 - Create profile
                                                    10 - Choice profile
                                        """;

    public Machine(CoffeeIngredientService ingredientList, BeverageService beverageList, int cleanlinessCount) {
        this.ingredientList = ingredientList;
        this.beverageList = beverageList;
        this.profiles = new ProfileService(new ArrayList<>(), new Scanner(System.in));

        this.cleanliness = cleanlinessCount;
        this.onOffButton = false;
    }

    private void switchOnOf() {
        this.onOffButton = !this.onOffButton;
    }

    private static void sendErrResponse(Exception errMsg) {
        System.out.printf("Sorry but something was wrong:\n %s", errMsg.getMessage());
        logger.error(errMsg.getMessage());
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

    private void cleanMachine() throws MachineException {
        if (this.cleanliness == 10) {
            throw new MachineException("Machine has already cleaned");
        }

        this.cleanliness = 10;
    }

    private void serveBeverage(Drink name, int cupCount) throws MachineException {
        if (this.cleanliness == 0) {
            throw new MachineException("Sorry but Coffee machine need to be clean\n");
        }

        Beverage beverage = this.beverageList.getBeverageByName(name);
        List<Ingredient> bvgIng = beverage.getIngredients();
        if (bvgIng == null) {
            throw new MachineException(String.format("Ingredient list of <%s> beverage is empty\n", beverage.getName()));
        }

        for (Ingredient bvIng : beverage.getIngredients()) {
            Ingredient cmIng = this.ingredientList.getIngredientByName(bvIng.getName());

            try {
                cmIng.addCount(-1*(bvIng.getCount() * cupCount));
            } catch (IngredientException e) {
                throw new MachineException(e.getMessage());
            }
        }

        this.cleanliness -= cupCount;
    }

    private String adapterServeBeverage(Drink drink) throws MachineException {
        System.out.printf("Please enter now many cup of %s you want\nOr press <3> to make 3 cup of %s\n", drink, drink);
        int cupCount = stream.nextInt();

        try {
            this.serveBeverage(drink, cupCount);
        } catch (MachineException e) {
            throw new MachineException(e.getMessage());
        }
        return String.format("a %d of %s", cupCount, drink);
    }

    private String choiceBeverage() throws MachineException {
        Drink drink;
        try {
            drink = this.profiles.getProfileUserPickBeverage(this.activeProfile, this.beverageList);
        } catch (MachineException e) {
            throw new MachineException(e.getMessage());
        }
        return this.adapterServeBeverage(drink);
    }

    private void showRecipes() throws MachineException {
        Drink userPick;
        try {
            userPick = this.profiles.getProfileUserPickBeverage(this.activeProfile, this.beverageList);
        } catch (MachineException e) {
            throw new MachineException(e.getMessage());
        }

        Beverage beverage = this.beverageList.getBeverageByName(userPick);
        List<Ingredient> bvgIng = beverage.getIngredients();
        if (bvgIng == null) {
            throw new MachineException(String.format("Ingredient list of <%s> beverage is empty", beverage.getName()));
        }

        System.out.printf("For %s, we need: \n", beverage.getName());
        for (Ingredient ing : beverage.getIngredients()) {
            System.out.printf("* --- %d of %s;\n", ing.getCount(), ing.getName());
        }
    }

    private void handleFunc(int cid) {
        Controls cmd = Controls.getCommandByCid(cid);
        if (cmd == null) {
            sendErrResponse(new MachineException(String.format("Was entered incorrect command: <%d>", cid)));
        }

        int count;
        String result = "";
        switch (Objects.requireNonNull(cmd)) {
            case START_MACHINE:
                this.switchOnOf();
                logger.info("Button was pushed");
                break;
            case ADD_WATER:
                System.out.println("Please enter count of ingredient");
                count = stream.nextInt();
                try {
                    this.ingredientList.addIngredients("water", count);
                    logger.info(String.format("To <%s> was added <%d>", "water", count));
                } catch (IngredientException e) {
                    sendErrResponse(e);
                }
                break;
            case ADD_COFFEE:
                System.out.println("Please enter count of ingredient");
                count = stream.nextInt();
                try {
                    this.ingredientList.addIngredients("coffee", count);
                    logger.info(String.format("To <%s> was added <%d>", "coffee", count));
                } catch (IngredientException e) {
                    sendErrResponse(e);
                }
                break;
            case ADD_MILK:
                System.out.println("Please enter count of ingredient");
                count = stream.nextInt();
                try {
                    this.ingredientList.addIngredients("milk", count);
                    logger.info(String.format("To <%s> was added <%d>", "milk", count));
                } catch (IngredientException e) {
                    sendErrResponse(e);
                }
                break;
            case CHECK_SYSTEM:
                System.out.printf("How is %d cleanliness\n", this.cleanliness);
                this.ingredientList.showIngredients();
                break;
            case CLEAN_MACHINE:
                try {
                    this.cleanMachine();
                    logger.info("Machine was cleaned!");
                } catch (MachineException e) {
                    sendErrResponse(e);
                }
                break;
            case CHOICE_BEVERAGE:
                try {
                    result = this.choiceBeverage();
                    logger.info(String.format("Was cooked <%s>", result));
                } catch (MachineException e) {
                    sendErrResponse(e);
                }
                break;
            case SHOW_RECIPE:
                try {
                    this.showRecipes();
                    logger.info("Was showed recipe");
                } catch (MachineException e) {
                    sendErrResponse(e);
                }
                break;
            case CREATE_PROFILE:
                try {
                    this.profiles.createProfile(this.beverageList);
                    logger.info("Was created profile");
                } catch (ProfileException e) {
                    sendErrResponse(e);
                }
                break;
            case CHOICE_PROFILE:
                System.out.println("Choose one of the available profile\n");
                this.profiles.showProfileList();
                int profileId = stream.nextInt();
                this.activeProfile = this.profiles.getProfile(profileId - 1);
                logger.info(String.format("Was choice profile <%s>", this.activeProfile.getName()));
                break;
            case MAKE_ESPRESSO:
                try {
                    result = this.adapterServeBeverage(Drink.ESPRESSO);
                    logger.info(String.format("Was cooked <%s>", result));
                } catch (MachineException e) {
                    sendErrResponse(e);
                }
                break;
            case MAKE_CAPPUCCINO:
                try {
                    result = this.adapterServeBeverage(Drink.CAPPUCCINO);
                    logger.info(String.format("Was cooked <%s>", result));
                } catch (MachineException e) {
                    sendErrResponse(e);
                }
                break;
            case EXIT_PROFILE:
                this.activeProfile = null;
                break;
        }
    }

    private void loadProfileMenu() {
        System.out.println("Enter the number of the corresponding command: ");
        for (Beverage beverage : this.beverageList.getBeverageList()) {
            if (this.activeProfile.isBeverageAtList(beverage)) {
                System.out.printf("%d - %s\n", (11+beverage.getName().ordinal()), beverage.getName());
            }
        }
        System.out.println("13 - Exit profile\n");
    }

    private boolean loadMainMenu() {
        if (this.activeProfile != null) {
            this.loadProfileMenu();
        } else {
            System.out.println(DEFAULT_MENU);
        }

        int cid = stream.nextInt();
        this.handleFunc(cid);

        return this.onOffButton;
    }

    public void entryPoint() {
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
