package com.dongs.settings;

import com.dongs.common.Paths;
import com.dongs.common.exception.InvalidExtensionException;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.IOException;

@Slf4j
public final class Settings {

    public static final String[] SETTING_FILE_PATHS = {
            Paths.getResourcesPathInMain("", "config", ".yml"),
            Paths.getResourcesPathInMain("", "config", ".yaml")
    };

    private Settings() {
    }

    public static Setting init() {
        Setting setting = Setting.getInstance();
        setting.init();
        return setting;
    }

    public static Setting readSettingFromConfigYaml() {
        Setting setting = Setting.getInstance();

        for (String settingFilePath : SETTING_FILE_PATHS) {
            boolean success = tryReadSettings(settingFilePath);
            if (success) return setting;
        }

        log.info("There is no configuration file");
        return setting;
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
