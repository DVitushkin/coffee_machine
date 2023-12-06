package org.DVitushkin.machine;

public enum Controls {
    START_MACHINE(1),
    ADD_WATER(2),
    ADD_COFFEE(3),
    ADD_MILK(4),
    CHECK_SYSTEM(5),
    CLEAN_MACHINE(6),
    CHOICE_BEVERAGE(7),
    SHOW_RECIPE(8),
    CREATE_PROFILE(9),
    CHOICE_PROFILE(10),
    MAKE_ESPRESSO(11),
    MAKE_CAPPUCCINO(12),
    EXIT_PROFILE(13);

    private final int cid;

    Controls(int cid) { this.cid = cid; }

    public static Controls getCommandByCid(int cid) {
        return Controls.values()[cid - 1];
    }
}
