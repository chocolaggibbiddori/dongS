package com.dongs.settings;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class YamlTest {

    @Test
    void readSettings() throws FileNotFoundException, InvalidExtensionException {
        String configPath = "src/test/resources/config.yml";
        Setting setting = Yaml.readSettings(configPath);
        Assertions.assertTrue(setting.schedule().autoRemove().value());
    }
}
