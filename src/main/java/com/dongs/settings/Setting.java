package com.dongs.settings;

public class Setting {

    private static final Setting INSTANCE = new Setting();

    private final Schedule schedule = Schedule.getInstance();

    private Setting() {
    }

    public static Setting getInstance() {
        return INSTANCE;
    }

    public Schedule schedule() {
        return schedule;
    }
}
