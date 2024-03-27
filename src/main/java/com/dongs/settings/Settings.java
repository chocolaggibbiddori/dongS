package com.dongs.settings;

import com.dongs.common.exception.InvalidExtensionException;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.IOException;

@Slf4j
public final class Settings {

    private Settings() {
    }

    public static Setting init() {
        Setting setting = Setting.getInstance();
        setting.init();
        return setting;
    }

    public static Setting readSettingFromConfigYaml() {
        if (!tryReadSettings("src/main/resources/config.yml") &&
            !tryReadSettings("src/main/resources/config.yaml"))
            log.info("There is no configuration file");

        return Setting.getInstance();
    }

    private static boolean tryReadSettings(String configPath) {
        try (YamlReader reader = new YamlReader(configPath)) {
            reader.readSettings();
            return true;
        } catch (FileNotFoundException | InvalidExtensionException e) {
            return false;
        } catch (IOException e) {
            log.warn(e.getMessage());
            return true;
        }
    }
}
