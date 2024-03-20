package com.dongs.scheduler;

import lombok.extern.java.Log;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Log
public class CSVReader {

    private CSVReader() {
    }

    public static List<Schedule> readSchedulesFromCSV(String filePath) {
        List<Schedule> scheduleList = new ArrayList<>();

        try (LineNumberReader reader = new LineNumberReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int lineNumber = reader.getLineNumber();
                try {
                    Schedule s = scheduleFromCSVString(line);
                    scheduleList.add(s);
                } catch (Exception e) {
                    log.warning(lineMessage(lineNumber) + e.getMessage());
                }
            }
        } catch (IOException e) {
            log.warning(e.getMessage());
        }

        return scheduleList;
    }

    private static Schedule scheduleFromCSVString(String line) {
        String[] parts = line.split("\\|");

        String title = parts[0].trim();
        LocalDate startDate = LocalDate.parse(parts[1].trim());
        LocalDate endDate = LocalDate.parse(parts[2].trim());
        DayOfWeek dayOfWeek = getDayOfWeek(parts[3].trim());
        LocalTime startTime = LocalTime.parse(parts[4].trim());
        LocalTime endTime = LocalTime.parse(parts[5].trim());

        if (isInvalidDate(startDate, endDate)) {
            throw new IllegalArgumentException("Illegal startDate and endDate! startDate must be before endDate");
        }

        if (isInvalidDayOfWeek(startDate, endDate, dayOfWeek)) {
            throw new IllegalArgumentException("Illegal dayOfWeek! dayOfWeek must exist between startDate and endDate");
        }

        if (isInvalidDate(LocalDate.now(), endDate)) {
            throw new IllegalArgumentException("Illegal endDate [%s]. It's already over".formatted(parts[2].trim()));
        }

        if (isInvalidTime(startTime, endTime)) {
            throw new IllegalArgumentException("Illegal startTime and endTime! startTime must be before endTime");
        }

        return Schedule.of(title, dayOfWeek, startTime, endTime, startDate, endDate);
    }

    private static DayOfWeek getDayOfWeek(String dayOfWeekStr) {
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

    private static boolean isInvalidDate(LocalDate before, LocalDate after) {
        return before.isAfter(after);
    }

    private static boolean isInvalidDayOfWeek(LocalDate startDate, LocalDate endDate, DayOfWeek dayOfWeek) {
        return startDate.datesUntil(endDate.plusDays(1))
                .noneMatch(d -> d.getDayOfWeek() == dayOfWeek);
    }

    private static boolean isInvalidTime(LocalTime before, LocalTime after) {
        return before.isAfter(after);
    }

    private static String lineMessage(int num) {
        return "Line %d: ".formatted(num);
    }
}
