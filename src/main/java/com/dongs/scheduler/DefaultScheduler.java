package com.dongs.scheduler;

import lombok.extern.java.Log;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;

@Log
final class DefaultScheduler extends EnumMap<DayOfWeek, Set<Schedule>> implements Scheduler {

    private static final DefaultScheduler INSTANCE = new DefaultScheduler(DayOfWeek.class);

    static {
        INSTANCE.put(DayOfWeek.MONDAY, new HashSet<>());
        INSTANCE.put(DayOfWeek.TUESDAY, new HashSet<>());
        INSTANCE.put(DayOfWeek.WEDNESDAY, new HashSet<>());
        INSTANCE.put(DayOfWeek.THURSDAY, new HashSet<>());
        INSTANCE.put(DayOfWeek.FRIDAY, new HashSet<>());
        INSTANCE.put(DayOfWeek.SATURDAY, new HashSet<>());
        INSTANCE.put(DayOfWeek.SUNDAY, new HashSet<>());
    }

    private DefaultScheduler(Class<DayOfWeek> keyType) {
        super(keyType);
    }

    public static DefaultScheduler getInstance() {
        return INSTANCE;
    }

    @Override
    public void readAndInspect() throws FileNotFoundException {
        List<Schedule> scheduleList = CsvReader.readSchedulesFromCsv();
        inspect(scheduleList);
    }

    private void inspect(Collection<Schedule> schedules) {
        for (Schedule schedule : schedules) {
            DayOfWeek key = schedule.getDayOfWeek();
            Set<Schedule> scheduleSet = get(key);
            boolean clash = false;

            for (Schedule sch : scheduleSet) {
                if (isClash(sch, schedule)) {
                    log.info("[Clash] \"%s\" is impossible. cause: %s".formatted(schedule.getTitle(), sch));
                    clash = true;
                    break;
                }
            }

            if (!clash) {
                scheduleSet.add(schedule);
            }
        }
    }

    private boolean isClash(Schedule s1, Schedule s2) {
        LocalDate s1StartDate = s1.getStartDate();
        LocalDate s1EndDate = s1.getEndDate();
        LocalDate s2StartDate = s2.getStartDate();
        LocalDate s2EndDate = s2.getEndDate();

        if (s2StartDate.isAfter(s1EndDate)) {
            return false;
        }

        if (s2EndDate.isBefore(s1StartDate)) {
            return false;
        }

        LocalTime s1StartTime = s1.getStartTime();
        LocalTime s1EndTime = s1.getEndTime();
        LocalTime s2StartTime = s2.getStartTime();
        LocalTime s2EndTime = s2.getEndTime();

        if ((s2StartTime.isAfter(s1StartTime) || s2StartTime.equals(s1StartTime)) && s2StartTime.isBefore(s1EndTime)) {
            return true;
        }

        if (s2StartTime.isBefore(s1StartTime) && s2EndTime.isAfter(s1StartTime)) {
            return true;
        }

        return false;
    }

    private static final class CsvReader {

        static List<Schedule> readSchedulesFromCsv() throws FileNotFoundException {
            // TODO [2024-03-21]: 자동 삭제 기능 추가 필요
            String csvPath = "src/main/resources/data.csv";
            List<Schedule> scheduleList = new ArrayList<>();

            try (LineNumberReader reader = new LineNumberReader(new FileReader(csvPath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.isBlank() || isAnnotation(line)) continue;

                    int lineNumber = reader.getLineNumber();
                    try {
                        Schedule s = scheduleFromCSVString(line);
                        scheduleList.add(s);
                    } catch (Exception e) {
                        log.warning(lineMessage(lineNumber, e.getMessage()));
                    }
                }
            } catch (FileNotFoundException e) {
                throw e;
            } catch (IOException e) {
                log.warning(e.getMessage());
            }

            return scheduleList;
        }

        private static boolean isAnnotation(String line) {
            return line.startsWith("//");
        }

        private static Schedule scheduleFromCSVString(String line) {
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
                default ->
                        throw new IllegalArgumentException("Illegal dayOfWeek [%s]. Write it correctly in [월,화,수,목,금,토,일]".formatted(dayOfWeekStr));
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

        private static String lineMessage(int num, String msg) {
            return "Line %d: %s".formatted(num, msg);
        }
    }
}
