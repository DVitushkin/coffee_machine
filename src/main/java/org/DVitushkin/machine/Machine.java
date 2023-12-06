package org.DVitushkin.machine;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.DVitushkin.beverage.Beverage;
import org.DVitushkin.customexception.IngredientException;
import org.DVitushkin.customexception.ProfileException;
import org.DVitushkin.ingredient.CoffeeMachineIng;
import org.DVitushkin.ingredient.Ingredient;
import org.DVitushkin.customexception.MachineException;
import org.DVitushkin.profile.Profile;


public class Machine {
    private final List<CoffeeMachineIng> ingredientList;
    private final List<Beverage> beverageList;
    private final List<Profile> profiles;

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

    public Machine(List<CoffeeMachineIng> ingredientList, List<Beverage> beverageList, int cleanlinessCount) {
        this.ingredientList = ingredientList;
        this.beverageList = beverageList;
        this.profiles = new ArrayList<>();

        this.cleanliness = cleanlinessCount;
        this.onOffButton = false;
    }

    private void switchOnOf() {
        this.onOffButton = !this.onOffButton;
    }

    private static void sendErrResponse(MachineException errMsg) {
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

    private void addIngredients(String name, int count) throws MachineException {
        Ingredient ing = this.getIngredientByName(name);
        try {
            ing.addCount(count);
        } catch (IngredientException e) {
            throw new MachineException(e.getMessage());
        }
    }

    private void showIngredients() {
        for (Ingredient ing : this.ingredientList) {
            System.out.printf("%s == %d%n", ing.getName(), ing.getCount());
        }
    }

    private Ingredient getIngredientByName(String name) {
        for (Ingredient ingredient : this.ingredientList) {
            if (name.equals(ingredient.getName())) {
                return ingredient;
            }
        }
        return null;
    }

    private Beverage getBeverageByName(Drink name) {
        for (Beverage beverage : this.beverageList) {
            if (name.equals(beverage.getName())) {
                return beverage;
            }
        }
        return null;
    }

    private void serveBeverage(Drink name, int cupCount) throws MachineException {
        if (this.cleanliness == 0) {
            throw new MachineException("Sorry but Coffee machine need to be clean");
        }

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
            drink = this.getUserPickBeverage(this.activeProfile);
        } catch (MachineException e) {
            throw new MachineException(e.getMessage());
        }
        return this.adapterServeBeverage(drink);
    }

    private Drink getUserPickBeverage(Profile profile) throws MachineException {
        System.out.println("Choose one of the available recipe\n");

        for (Beverage bvg : this.beverageList) {
            if (profile != null) {
                if (profile.isBeverageAtList(bvg)) {
                    continue;
                }
            }
            System.out.printf("%d - %s\n", bvg.getName().ordinal()+1, bvg.getName());
        }

        int id = stream.nextInt();
        try {
            return Drink.getDrinkById(id);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new MachineException(e.getMessage());
        }
    }

    private void addBeverageToProfile(Profile newProfile) throws MachineException {
        Drink drink;
        try {
            drink = this.getUserPickBeverage(newProfile);
        } catch (MachineException e) {
            throw new MachineException(e.getMessage());
        }
        Beverage beverage = this.getBeverageByName(drink);

        try {
            newProfile.addBeverage(beverage);
        } catch (ProfileException e) {
            throw new MachineException(e.getMessage());
        }
    }

    private void createProfile() throws MachineException {
        System.out.println("Enter name for profile");
        String name = stream.nextLine();
        name = stream.nextLine();

        Profile newProfile = new Profile(name);
        try {
            this.addBeverageToProfile(newProfile);
        } catch (MachineException e) {
            throw new MachineException(e.getMessage());
        }
        this.profiles.add(newProfile);

        int counter = this.beverageList.size() -1;
        while ( counter != 0) {
            System.out.println("Do you want to add another one?\n1 - YES\n2 - NO");
            int cmd = stream.nextInt();
            switch (cmd) {
                case 1:
                    try {
                        this.addBeverageToProfile(newProfile);
                        counter--;
                    } catch (MachineException e) {
                        throw new MachineException(e.getMessage());
                    }
                    break;
                case 2:
                    counter = 0;
                    break;
                default:
                    throw new MachineException("Was sent incorrect command");
            }
        }
    }

    private void showProfileList() {
        for (int i = 0; i < this.profiles.size(); i++) {
            System.out.printf("%d - %s\n", (i+1), profiles.get(i).getName());
        }
    }

    private void showRecipes() throws MachineException {
        Drink userPick;
        try {
            userPick = this.getUserPickBeverage(this.activeProfile);
        } catch (MachineException e) {
            throw new MachineException(e.getMessage());
        }

        Beverage beverage = this.getBeverageByName(userPick);

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
                    this.addIngredients("water", count);
                    logger.info(String.format("To <%s> was added <%d>", "water", count));
                } catch (MachineException e) {
                    sendErrResponse(e);
                }
                break;
            case ADD_COFFEE:
                System.out.println("Please enter count of ingredient");
                count = stream.nextInt();
                try {
                    this.addIngredients("coffee", count);
                    logger.info(String.format("To <%s> was added <%d>", "coffee", count));
                } catch (MachineException e) {
                    sendErrResponse(e);
                }
                break;
            case ADD_MILK:
                System.out.println("Please enter count of ingredient");
                count = stream.nextInt();
                try {
                    this.addIngredients("milk", count);
                    logger.info(String.format("To <%s> was added <%d>", "milk", count));
                } catch (MachineException e) {
                    sendErrResponse(e);
                }
                break;
            case CHECK_SYSTEM:
                System.out.printf("How is %d cleanliness\n", this.cleanliness);
                this.showIngredients();
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
                    this.createProfile();
                    logger.info("Was created profile");
                } catch (MachineException e) {
                    sendErrResponse(e);
                }
                break;
            case CHOICE_PROFILE: // here
                System.out.println("Choose one of the available profile\n");
                this.showProfileList();
                int profileId = stream.nextInt();
                this.activeProfile = this.profiles.get(profileId - 1);
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
        for (Beverage beverage : this.beverageList) {
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
