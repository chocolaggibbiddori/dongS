package com.dongs.scheduler;

import com.dongs.common.exception.InvalidExtensionException;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Slf4j
class CsvReader extends LineNumberReader {

    CsvReader(String csvPath) throws FileNotFoundException, InvalidExtensionException {
        super(new FileReader(csvPath));
        checkExtension(csvPath);
    }

    private void checkExtension(String csvPath) throws InvalidExtensionException {
        if (!csvPath.endsWith(".csv")) throw new InvalidExtensionException("Data file must have '.csv' extension");
    }

    List<Schedule> readSchedules() {
        try (Stream<String> lines = lines()) {
            return lines
                    .filter(l -> !l.isBlank())
                    .filter(l -> !isAnnotation(l))
                    .map(this::convertToSchedule)
                    .filter(Objects::nonNull)
                    .toList();
        }
    }

    private boolean isAnnotation(String line) {
        return line.startsWith("//");
    }

    private Schedule convertToSchedule(String line) {
        try {
            return scheduleFromCsvString(line);
        } catch (IllegalArgumentException e) {
            int lineNumber = getLineNumber();
            String message = e.getMessage();
            log.warn("Line {}: {}", lineNumber, message);
            return null;
        }
    }

    private Schedule scheduleFromCsvString(String line) {
        String[] parts = line.split("\\|");

        String title;
        LocalDate startDate;
        LocalDate endDate;
        DayOfWeek dayOfWeek;
        LocalTime startTime;
        LocalTime endTime;

        try {
            title = parts[0].trim();
            startDate = LocalDate.parse(parts[1].trim());
            endDate = LocalDate.parse(parts[2].trim());
            dayOfWeek = getDayOfWeek(parts[3].trim());
            startTime = LocalTime.parse(parts[4].trim());
            endTime = LocalTime.parse(parts[5].trim());
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Illegal data! Please fill in the appropriate data in the form");
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Illegal data! The date or time format is incorrect. The date format is 'yyyy-MM-dd' and the time format is 'hh:mm'");
        }

        if (isInvalidDate(startDate, endDate)) {
            throw new IllegalArgumentException("Illegal startDate and endDate! startDate must be before endDate");
        }

        if (isInvalidDayOfWeek(startDate, endDate, dayOfWeek)) {
            throw new IllegalArgumentException("Illegal dayOfWeek! dayOfWeek must exist between startDate and endDate");
        }

        if (isInvalidTime(startTime, endTime)) {
            throw new IllegalArgumentException("Illegal startTime and endTime! startTime must be before endTime");
        }

        if (isInvalidDate(LocalDate.now(), endDate)) {
            log.info("Illegal endDate [%s]. It's already over".formatted(endDate));
        }

        return Schedule.of(title, dayOfWeek, startTime, endTime, startDate, endDate);
    }

    private DayOfWeek getDayOfWeek(String dayOfWeekStr) {
        return switch (dayOfWeekStr) {
            case "월" -> DayOfWeek.MONDAY;
            case "화" -> DayOfWeek.TUESDAY;
            case "수" -> DayOfWeek.WEDNESDAY;
            case "목" -> DayOfWeek.THURSDAY;
            case "금" -> DayOfWeek.FRIDAY;
            case "토" -> DayOfWeek.SATURDAY;
            case "일" -> DayOfWeek.SUNDAY;
            default -> throw new IllegalArgumentException("Illegal dayOfWeek [%s]. Write it correctly in [월,화,수,목,금,토,일]".formatted(dayOfWeekStr));
        };
    }

    private boolean isInvalidDate(LocalDate before, LocalDate after) {
        return before.isAfter(after);
    }

    private boolean isInvalidDayOfWeek(LocalDate startDate, LocalDate endDate, DayOfWeek dayOfWeek) {
        return startDate.datesUntil(endDate.plusDays(1))
                .noneMatch(d -> d.getDayOfWeek() == dayOfWeek);
    }

    private boolean isInvalidTime(LocalTime before, LocalTime after) {
        return before.isAfter(after);
    }
}
