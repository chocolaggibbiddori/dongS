package com.dongs.settings;

record Filename(String value) implements ChildSetting<Filename, String> {

    static final Filename DEFAULT = new Filename("data");

    @Override
    public Filename getDefault() {
        return DEFAULT;
    }
}
