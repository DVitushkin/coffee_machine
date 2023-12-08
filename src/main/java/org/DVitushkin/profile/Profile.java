package org.DVitushkin.profile;

import org.DVitushkin.beverage.Beverage;
import org.DVitushkin.customexception.ProfileException;

import javax.swing.border.BevelBorder;
import java.util.ArrayList;
import java.util.List;

public class Profile {
    private final String name;
    private final List<Beverage> pinnedBeverage;
    private final int cupCount;

    public Profile(String name, int cupCount) {
        this.name = name;
        this.cupCount = cupCount;
        this.pinnedBeverage = new ArrayList<Beverage>();
    }

    public String getName() {
        return name;
    }

    public List<Beverage> getPinnedBeverage() {
        return pinnedBeverage;
    }

    public int getCupCount() {
        return cupCount;
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
