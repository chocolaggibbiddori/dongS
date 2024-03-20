package com.dongs.settings;

import com.dongs.settings.interfaces.BooleanValueSetting;

record AutoRemove(boolean value) implements BooleanValueSetting {

    static final AutoRemove TRUE = new AutoRemove(true);
    static final AutoRemove FALSE = new AutoRemove(false);
    static final AutoRemove DEFAULT = FALSE;
}
