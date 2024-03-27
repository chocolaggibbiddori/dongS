package com.dongs.settings;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Setting {

    private static final Setting INSTANCE = new Setting();

    private final Schedule schedule = Schedule.getInstance();
    private final Data data = Data.getInstance();

    private Setting() {
    }

    public static Setting getInstance() {
        return INSTANCE;
    }

    public Schedule schedule() {
        return schedule;
    }

    public Data data() {
        return data;
    }
}
