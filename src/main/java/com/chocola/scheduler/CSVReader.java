package com.chocola.scheduler;

import lombok.extern.java.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log
public class CSVReader {

    private CSVReader() {
    }

    public static List<Schedule> readSchedulesFromCSV(String filePath) {
        List<Schedule> scheduleList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine();

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
        Pattern pattern = Pattern.compile("\"([^\"]*)\"|([^,]+)");
        Matcher matcher = pattern.matcher(line);

        String[] parts = new String[6];
        for (int i = 0; i < parts.length && matcher.find(); i++) {
            parts[i] = matcher.group(1) != null ? matcher.group(1) : matcher.group(2);
        }

        String titleStr = parts[0];
        String startDateStr = parts[1];
        String endDateStr = parts[2];
        String dayOfWeekStr = parts[3];
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
        String startTimeStr = parts[4];
        String endTimeStr = parts[5];

        String title = titleStr.replaceAll("\"", "");
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
