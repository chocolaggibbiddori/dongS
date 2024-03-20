package com.dongs.settings;

import lombok.Getter;

@Getter
public class Setting {

    private static final Setting INSTANCE = new Setting();

    private final Schedule schedule = Schedule.getInstance();

    private Setting() {
    }

    public static Setting getInstance() {
        return INSTANCE;
    }
}
