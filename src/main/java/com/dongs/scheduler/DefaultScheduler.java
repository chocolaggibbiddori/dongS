package com.dongs.scheduler;

import lombok.extern.java.Log;

import java.io.FileNotFoundException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
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
        String csvPath = "src/main/resources/data.csv";
        List<Schedule> scheduleList = CsvReader.readSchedulesFromCsv(csvPath);
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
}
