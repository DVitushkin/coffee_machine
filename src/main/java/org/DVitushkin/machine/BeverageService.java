package org.DVitushkin.machine;

import org.DVitushkin.beverage.Beverage;

import java.util.List;

public class BeverageService {
    private final List<Beverage> beverageList;

    public BeverageService(List<Beverage> beverageList) {
        this.beverageList = beverageList;
    }

    public List<Beverage> getBeverageList() {
        return beverageList;
    }

    public Beverage getBeverageByName(Drink name) {
        for (Beverage beverage : this.beverageList) {
            if (name.equals(beverage.getName())) {
                return beverage;
            }
        }
        return new Beverage(name, null);
    }
}
