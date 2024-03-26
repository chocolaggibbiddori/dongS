package com.dongs.settings;

import com.dongs.common.exception.InvalidExtensionException;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;

@Slf4j
public class Setting {

    private static final Setting INSTANCE = new Setting();

    private final Schedule schedule = Schedule.getInstance();
    private final Data data = Data.getInstance();

    private Setting() {
        if (tryReadSettings("src/main/resources/config.yml") ||
            tryReadSettings("src/main/resources/config.yaml"))
            return;

        log.info("There is no configuration file");
    }

    private boolean tryReadSettings(String configPath) {
        try {
            Yaml.readSettings(configPath);
            return true;
        } catch (FileNotFoundException | InvalidExtensionException e) {
            return false;
        }
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
