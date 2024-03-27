package com.dongs.settings;

import com.dongs.settings.interfaces.BooleanValueSetting;

public final class Schedule extends AbstractParentSetting {

    private static final Schedule INSTANCE = new Schedule();

    private AutoRemove autoRemove = AutoRemove.FALSE;

    private Schedule() {
    }

    static Schedule getInstance() {
        return INSTANCE;
    }

    void setAutoRemove(boolean autoRemove) {
        this.autoRemove = autoRemove ? AutoRemove.TRUE : AutoRemove.FALSE;
    }

    public BooleanValueSetting autoRemove() {
        return autoRemove;
    }
}
