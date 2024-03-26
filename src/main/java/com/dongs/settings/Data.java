package com.dongs.settings;

import com.dongs.settings.interfaces.ValueSetting;

public class Data {

    private static final Data INSTANCE = new Data();

    private Filename filename = Filename.DEFAULT;

    private Data() {
    }

    static Data getInstance() {
        return INSTANCE;
    }

    void setFilename(String filename) {
        this.filename = new Filename(filename);
    }

    public ValueSetting<String> filename() {
        return filename;
    }
}
