package org.DVitushkin.machine;

import org.DVitushkin.beverage.Beverage;
import org.DVitushkin.customexception.MachineException;
import org.DVitushkin.customexception.ProfileException;
import org.DVitushkin.ingredient.Ingredient;
import org.DVitushkin.profile.Profile;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ProfileService {
    private final List<Profile> profiles;
    private final Scanner stream;

    public ProfileService(List<Profile> profiles, Scanner stream) {
        this.profiles = profiles;
        this.stream = stream;
    }

    public void addProfile(Profile newProfile) {
        this.profiles.add(newProfile);
    }

    public Profile getProfile(int index) {
        return this.profiles.get(index);
    }

    public void showProfileList() {
        for (int i = 0; i < this.profiles.size(); i++) {
            System.out.printf("%d - %s\n", (i+1), profiles.get(i).getName());
        }
    }

    public Drink getProfileUserPickBeverage(Profile profile, BeverageService beverageList) throws MachineException {
        System.out.println("Choose one of the available recipe\n");

        for (Beverage bvg : beverageList.getBeverageList()) {
            if (profile != null) {
                if (profile.isBeverageAtList(bvg)) {
                    continue;
                }
            }
            System.out.printf("%d - %s\n", bvg.getName().ordinal()+1, bvg.getName());
        }

        int id = this.stream.nextInt();
        try {
            return Drink.getDrinkById(id);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Was input incorrect button try again");
            return this.getProfileUserPickBeverage(profile, beverageList);
        }
    }

    public void addBeverageToProfile(Profile newProfile, BeverageService beverageList) throws ProfileException {
        Drink drink;
        try {
            drink = this.getProfileUserPickBeverage(newProfile, beverageList);
        } catch (MachineException e) {
            throw new ProfileException(e.getMessage());
        }
        Beverage beverage = beverageList.getBeverageByName(drink);
        List<Ingredient> bvgIng = beverage.getIngredients();
        if (bvgIng == null) {
            throw new ProfileException(String.format("Ingredient list of <%s> beverage is empty", beverage.getName()));
        }
        
        var result = newProfile.addBeverage(beverage);
        if (!result) {
            addBeverageToProfile(newProfile, beverageList);
        }
    }

    public void createProfile(BeverageService beverageList) throws ProfileException {
        System.out.println("Enter name for profile:");
        String name = this.stream.next();

        System.out.println("Enter how cup do you want:");
        int cupCount = Integer.parseInt(this.stream.next());

        Profile newProfile = new Profile(name, cupCount);

        try {
            this.addBeverageToProfile(newProfile, beverageList);
        } catch (ProfileException e) {
            throw new ProfileException(e.getMessage());
        }
        this.addProfile(newProfile);

        int counter = beverageList.getBeverageList().size() -1;
        while ( counter != 0) {
            System.out.println("Do you want to add another one?\n1 - YES\n2 - NO");
            int cmd = this.stream.nextInt();
            switch (cmd) {
                case 1:
                    try {
                        this.addBeverageToProfile(newProfile, beverageList);
                        counter--;
                    } catch (ProfileException e) {
                        throw new ProfileException(e.getMessage());
                    }
                    break;
                case 2:
                    counter = 0;
                    break;
                default:
                    throw new ProfileException("Was sent incorrect command");
            }
        }
    }
}
