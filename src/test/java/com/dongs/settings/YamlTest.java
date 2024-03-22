package com.dongs.settings;

import com.dongs.common.exception.InvalidExtensionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

class YamlTest {

    @Test
    void readSettings() throws InvalidExtensionException, FileNotFoundException {
        String configPath = "src/test/resources/config.yml";
        Setting setting = Yaml.readSettings(configPath);
        Assertions.assertTrue(setting.schedule().autoRemove().value());
    }
}
