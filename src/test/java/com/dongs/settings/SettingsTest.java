package com.dongs.settings;

import com.dongs.common.exception.InvalidExtensionException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

class SettingsTest {

    @Test
    void init() throws IOException, InvalidExtensionException {
        //g
        String configPath = "src/test/resources/config.yml";
        try (YamlReader reader = new YamlReader(configPath)) {
            reader.readSettings();
        }

        //w
        Setting setting = Settings.init();

        //t
        assertFalse(setting.schedule().autoRemove().value());
        assertThat(setting.data().filename().value()).isEqualTo("data");
    }
}
