package com.dongs.settings;

import com.dongs.common.exception.InvalidExtensionException;
import lombok.extern.java.Log;

import java.io.FileNotFoundException;

@Log
public class Setting {

    private static final Setting INSTANCE = new Setting();

    private final Schedule schedule = Schedule.getInstance();

    private Setting() {
        String prefix = "src/main/resources/";
        String filename = "config";

        try {
            String firstPath = getPath(prefix, filename, ".yml");
            String secondPath = getPath(prefix, filename, ".yaml");
            if (tryReadSettings(firstPath) || tryReadSettings(secondPath)) return;
        } catch (InvalidExtensionException ignored) {
        }

        log.info("There is no configuration file");
    }

    private boolean tryReadSettings(String configPath) throws InvalidExtensionException {
        try {
            Yaml.readSettings(configPath);
            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    private String getPath(String prefix, String filename, String suffix) {
        return prefix + filename + suffix;
    }

    public static Setting getInstance() {
        return INSTANCE;
    }

    public Schedule schedule() {
        return schedule;
    }
}
