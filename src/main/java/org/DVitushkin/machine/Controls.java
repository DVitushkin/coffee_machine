package org.DVitushkin.machine;

public enum Controls {
    START_MACHINE(1),
    ADD_WATER(2),
    ADD_COFFEE(3),
    ADD_MILK(4),
    CHECK_SYSTEM(5),
    CHOICE_BEVERAGE(6),
    CREATE_PROFILE(7),
    CHOICE_PROFILE(8),
    SHOW_RECIPE(9);

    private final int cid;

    Controls(int cid) { this.cid = cid; }

    public static Controls getCommandByCid(int cid) {
        return Controls.values()[cid - 1];
    }
}
