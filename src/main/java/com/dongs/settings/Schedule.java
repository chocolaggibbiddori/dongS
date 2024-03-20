package com.dongs.settings;

import com.dongs.settings.interfaces.BooleanValueSetting;

public class Schedule {

    private static final Schedule INSTANCE = new Schedule();

    private AutoRemove autoRemove = AutoRemove.DEFAULT;

    private Schedule() {
    }

    static Schedule getInstance() {
        return INSTANCE;
    }

    void setAutoRemove(boolean autoRemove) {
        this.autoRemove = autoRemove ? AutoRemove.TRUE : AutoRemove.FALSE;
    }

    public BooleanValueSetting getAutoRemove() {
        return autoRemove;
    }
}
