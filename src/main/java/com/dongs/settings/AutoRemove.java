package com.dongs.settings;

record AutoRemove(boolean value) implements BooleanChildSetting<AutoRemove> {

    static final AutoRemove TRUE = new AutoRemove(true);
    static final AutoRemove FALSE = new AutoRemove(false);

    @Override
    public AutoRemove getDefault() {
        return FALSE;
    }
}
