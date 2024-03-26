package com.dongs.settings;

import com.dongs.common.exception.InvalidExtensionException;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class YamlTest {

    @Test
    void readSettings() throws InvalidExtensionException, FileNotFoundException {
        //g
        String configPath = "src/test/resources/config.yml";

        //w
        Setting setting = Yaml.readSettings(configPath);

        //t
        assertTrue(setting.schedule().autoRemove().value());
        assertThat(setting.data().filename().value()).isEqualTo("config");
    }
}
