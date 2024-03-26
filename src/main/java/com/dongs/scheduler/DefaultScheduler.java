package com.dongs.scheduler;

import com.dongs.common.exception.InvalidExtensionException;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Slf4j
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
    public void readAndInspect(String csvPath) throws InvalidExtensionException, FileNotFoundException {
        // TODO [2024-03-21]: 자동 삭제 기능 추가 필요
        Objects.requireNonNull(csvPath, "csvPath is null");

        try (CsvReader reader = new CsvReader(csvPath)) {
            List<Schedule> scheduleList = reader.readSchedules();
            inspect(scheduleList);
        } catch (InvalidExtensionException | FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
            log.warn(e.getMessage());
        }
    }

    private void inspect(Collection<Schedule> schedules) {
        for (Schedule schedule : schedules) {
            DayOfWeek key = schedule.getDayOfWeek();
            Set<Schedule> scheduleSet = get(key);
            boolean clash = false;

            for (Schedule sch : scheduleSet) {
                if (isClash(sch, schedule)) {
                    log.info("[Clash] \"{}\" is impossible. Cause: {}", schedule.getTitle(), sch);
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
