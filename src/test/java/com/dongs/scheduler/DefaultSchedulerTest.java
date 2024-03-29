package com.dongs.scheduler;

import com.dongs.common.exception.InvalidExtensionException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.FileNotFoundException;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DefaultSchedulerTest {

    private final DefaultScheduler scheduler = DefaultScheduler.getInstance();

    @AfterEach
    void tearDown() {
        scheduler.values().forEach(Set::clear);
    }

    @Test
    @DisplayName("매개변수가 null")
    void readAndInspect_throw_argIsNull() {
        assertThatThrownBy(() -> scheduler.readAndInspect(null))
                .hasMessage("csvPath is null")
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("데이터 파일이 없음")
    void readAndInspect_throw_noFile() {
        String csvPath = getPath("no-file.csv");
        assertThatThrownBy(() -> scheduler.readAndInspect(csvPath))
                .isInstanceOf(FileNotFoundException.class);
    }

    @Test
    @DisplayName("csv 파일이 아님")
    void readAndInspect_throw_invalidExtension() {
        String csvPath = getPath("file.yml");
        assertThatThrownBy(() -> scheduler.readAndInspect(csvPath))
                .isInstanceOf(InvalidExtensionException.class);
    }

    @ParameterizedTest(name = "filename={0}, answer={1}")
    @CsvSource({"data1.csv, 2", "data2.csv, 1", "data3.csv, 4"})
    @DisplayName("일정 충돌")
    void readAndInspect_clash(String filename, int answer) throws FileNotFoundException, InvalidExtensionException {
        //g
        String csvPath = getPath(filename);

        //w
        scheduler.readAndInspect(csvPath);

        //t
        int scheduleNum = scheduler.values().stream()
                .mapToInt(Set::size)
                .sum();

        assertThat(scheduleNum).isEqualTo(answer);
    }

    private String getPath(String filename) {
        String prefix = "src/test/resources/scheduler/default-scheduler/";
        return prefix + filename;
    }
}
