package com.dongs.settings;

import com.dongs.common.exception.InvalidExtensionException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class YamlReaderTest {

    @Test
    void readSettings() throws IOException, InvalidExtensionException {
        //g
        String configPath = "src/test/resources/config.yml";
        Setting setting;

        //w
        try (YamlReader reader = new YamlReader(configPath)) {
            setting = reader.readSettings();
        }

        //t
        assertTrue(setting.schedule().autoRemove().value());
        assertThat(setting.data().filename().value()).isEqualTo("config");
    }
}
