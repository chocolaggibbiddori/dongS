package com.dongs.settings;

import com.dongs.settings.interfaces.ValueSetting;

record Filename(String value) implements ValueSetting<String> {

    static final Filename DEFAULT = new Filename("data");
}
