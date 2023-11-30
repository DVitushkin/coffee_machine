package org.DVitushkin.ingredient;

import org.DVitushkin.machineException.MachineException;

public class Ingredient {
    private final String name;
    private int count;
    private final int maxCount;

    public Ingredient(String name, int count, int maxCount) {
        this.name = name;
        this.count = count;
        this.maxCount = maxCount;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) throws MachineException {
        if (count > maxCount || count < 0) {
            throw new MachineException(String.format("Incorrect count of %s", this.name));
        }
        this.count = count;
    }
}
