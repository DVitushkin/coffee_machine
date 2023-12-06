package org.DVitushkin.profile;

import org.DVitushkin.beverage.Beverage;
import org.DVitushkin.customexception.ProfileException;

import javax.swing.border.BevelBorder;
import java.util.ArrayList;
import java.util.List;

public class Profile {
    private final String name;
    private final List<Beverage> pinnedBeverage;

    public Profile(String name, List<Beverage> beverageList) {
        this.name = name;
        this.pinnedBeverage = beverageList;
    }

    public Profile(String name) {
        this.name = name;
        this.pinnedBeverage = new ArrayList<Beverage>();
    }

    public String getName() {
        return name;
    }

    public List<Beverage> getPinnedBeverage() {
        return this.pinnedBeverage;
    }

    public void addBeverage(Beverage newBvg) throws ProfileException {
        for (Beverage bvg : this.pinnedBeverage) {
            if (bvg.equals(newBvg)) {
                throw new ProfileException(String.format("<%s> already added to bvg list of this profile", newBvg.getName()));
            }
        }
        this.pinnedBeverage.add(newBvg);
    }

    public boolean isBeverageAtList(Beverage externalBvg) {
        for (Beverage innerBvg : this.pinnedBeverage) {
            if (externalBvg.equals(innerBvg)) {
                return true;
            }
        }
        return false;
    }
}
