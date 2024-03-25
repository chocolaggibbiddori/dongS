package com.dongs.scheduler;

import com.dongs.common.exception.InvalidExtensionException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

class CsvReaderTest {

    private CsvReader reader;

    @BeforeEach
    void setUp() throws FileNotFoundException, InvalidExtensionException {
        reader = new CsvReader("src/test/resources/scheduler/csv-reader/data.csv");
    }

    @AfterEach
    void tearDown() throws IOException {
        reader.close();
    }

    @Test
    void readSchedules() {
        //g
        List<Schedule> scheduleList = reader.readSchedules();

        //w
        int size = scheduleList.size();

        //t
        Assertions.assertThat(size).isOne();
    }
}
