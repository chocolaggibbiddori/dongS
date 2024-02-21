package com.dongs.scheduler;

import lombok.extern.java.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                try {
                    Schedule s = scheduleFromCSVString(lineNumber, line);
                    scheduleList.add(s);
                } catch (Exception e) {
                    log.warning(lineMessage(lineNumber) + e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return scheduleList;
    }

    private static Schedule scheduleFromCSVString(int lineNumber, String line) {
        String[] parts = line.split("\\|");

        String title = parts[0].trim();
        String startDateStr = parts[1].trim();
        String endDateStr = parts[2].trim();
        String dayOfWeekStr = parts[3].trim();
        dayOfWeekStr = switch (dayOfWeekStr) {
            case "월" -> DayOfWeek.MONDAY.toString();
            case "화" -> DayOfWeek.TUESDAY.toString();
            case "수" -> DayOfWeek.WEDNESDAY.toString();
            case "목" -> DayOfWeek.THURSDAY.toString();
            case "금" -> DayOfWeek.FRIDAY.toString();
            case "토" -> DayOfWeek.SATURDAY.toString();
            case "일" -> DayOfWeek.SUNDAY.toString();
            default -> throw new IllegalArgumentException(lineMessage(lineNumber) + "Illegal dayOfWeek [" + dayOfWeekStr + "]. Write it correctly in [월,화,수,목,금,토,일].");
        };
        String startTimeStr = parts[4].trim();
        String endTimeStr = parts[5].trim();

        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = LocalDate.parse(endDateStr);
        DayOfWeek dayOfWeek = DayOfWeek.valueOf(dayOfWeekStr);
        LocalTime startTime = LocalTime.parse(startTimeStr);
        LocalTime endTime = LocalTime.parse(endTimeStr);

        if (isInvalidDate(startDate, endDate)) {
            throw new IllegalArgumentException("Illegal startDate and endDate! startDate must be before endDate");
        }

        if (isInvalidDate(LocalDate.now(), endDate)) {
            throw new IllegalArgumentException("Illegal endDate [" + endDateStr + "]. It's already over");
        }

        if (isInvalidTime(startTime, endTime)) {
            throw new IllegalArgumentException("Illegal startTime and endTime! startTime must be before endTime");
        }

        return Schedule.of(title, dayOfWeek, startTime, endTime, startDate, endDate);
    }

    private static boolean isInvalidDate(LocalDate before, LocalDate after) {
        return before.isAfter(after);
    }

    private static boolean isInvalidTime(LocalTime before, LocalTime after) {
        return before.isAfter(after);
    }

    private static String lineMessage(int num) {
        return "Line " + num + ": ";
    }
}
